package com.payments.programs

import com.payments.algebras.PaymentCreationAlg
import com.payments.algebras.ServicesAlg
import com.payments.domain.Payment
import com.payments.domain.ServiceError
import cats.effect.IO

final case class PaymentCreation private (services: ServicesAlg) extends PaymentCreationAlg {
  import services._

  def create(payment: Payment): IO[Either[ServiceError, Payment]] =
    IO.pure(for {
      _ <- amountService.validate(payment.amount)
      _ <- transactionService.validate(payment.name, payment.recipient)
    } yield payment)
}

object PaymentCreation {
  def build(services: ServicesAlg) = new PaymentCreation(services)
}
