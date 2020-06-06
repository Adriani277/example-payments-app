package com.payments.interpreters

import com.payments.domain._
import com.payments.algebras.TransactionValidationAlg
import cats.implicits._

object TransactionValidation extends TransactionValidationAlg {
  def validate(sender: Name, recipient: Recipient): Either[InvalidTransactionError.type, Unit] =
    Either.cond(sender.value =!= recipient.value, (), InvalidTransactionError)
}
