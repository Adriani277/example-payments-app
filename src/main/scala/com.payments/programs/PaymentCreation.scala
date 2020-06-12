package com.payments.programs

import com.payments.algebras.PaymentCreationAlg
import com.payments.algebras.ServicesAlg
import com.payments.domain.Payment
import com.payments.domain.ServiceError
import cats.effect.IO
import cats.implicits._
import com.payments.domain.db.PaymentData

final case class PaymentCreation private (private val services: ServicesAlg[PaymentData])
    extends PaymentCreationAlg {
  import services._

  def create(payment: Payment): IO[Either[ServiceError, PaymentData]] = {
    val validated: Either[ServiceError, Payment] = for {
      _ <- amountService.validate(payment.amount)
      _ <- transactionService.validate(payment.name, payment.recipient)
    } yield payment

    val creation: Either[ServiceError, IO[PaymentData]] = validated.map(paymentRepoService.create)

    /**
      * We use sequence in order to flip the types around
      * since creation returns Either[ServiceError, IO[Payment]]
      * and we require         IO[Either[ServiceError, Payment]]
      *
      * sequence will give us the required type
      */
    creation.sequence
  }
}
