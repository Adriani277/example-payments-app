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

    validated.map(paymentRepoService.create) match {
      case Left(error)  => IO.pure(error.asLeft)
      case Right(value) => value.map(_.asRight)
    }
  }
}
