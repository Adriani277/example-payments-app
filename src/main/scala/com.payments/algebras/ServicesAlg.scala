package com.payments.algebras

trait ServicesAlg {
  def transactionService: TransactionValidationAlg
  def amountService: AmountValidationAlg
}
