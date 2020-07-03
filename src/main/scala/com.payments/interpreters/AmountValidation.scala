package com.payments.interpreters

import com.payments.algebras.AmountValidationAlg
import com.payments.domain.{InvalidAmountError, Amount}
import cats.implicits._

object AmountValidation extends AmountValidationAlg {
  def validate(amount: Amount): Either[InvalidAmountError, Unit] =
    if (amount.value <= 0 || amount.value > 1000000)
      InvalidAmountError(amount).asLeft
    else
      ().asRight

}
