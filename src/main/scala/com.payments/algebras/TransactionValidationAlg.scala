package com.payments.algebras

import com.payments.domain._

trait TransactionValidationAlg {
  def validate(sender: Name, recipient: Recipient): Either[InvalidTransactionError.type, Unit]
}
