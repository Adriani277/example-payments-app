package com.payments.programs

import com.payments.algebras.PaymentCreationAlg
import com.payments.domain.Payment
import com.payments.domain.ServiceError
import cats.effect.IO
import cats.implicits._
import com.payments.algebras.AmountValidationAlg
import com.payments.interpreters.AmountValidation

final case class PaymentCreation private (amountValidator: AmountValidationAlg)
    extends PaymentCreationAlg {
  def create(payment: Payment): IO[Either[ServiceError, Payment]] =
    IO.pure {
      amountValidator.validate(payment.amount) match {
        case Left(value) => value.asLeft[Payment]
        case Right(_)    => payment.asRight[ServiceError]
      }
    }
}

object PaymentCreation {
  val build = new PaymentCreation(AmountValidation)
}
