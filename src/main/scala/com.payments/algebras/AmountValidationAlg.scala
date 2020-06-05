package com.payments.algebras

import com.payments.domain.{Amount, InvalidAmountError}

trait AmountValidationAlg {
  def validate(amount: Amount): Either[InvalidAmountError, Unit]
}
