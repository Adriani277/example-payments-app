package com.payments.algebras

trait ServicesAlg[A] {
  def transactionService: TransactionValidationAlg
  def amountService: AmountValidationAlg
  def paymentRepoService: PaymentRepositoryAlg[A]
}
