package com.payments.interpreters

import com.payments.algebras.ServicesAlg
import com.payments.algebras.AmountValidationAlg
import com.payments.algebras.TransactionValidationAlg
import com.payments.algebras.PaymentRepositoryAlg

object Services {
  def build[A](
      amountValidation: AmountValidationAlg,
      transactionValidation: TransactionValidationAlg,
      paymentRepo: PaymentRepositoryAlg[A]
  ): ServicesAlg[A] =
    new ServicesAlg[A] {
      def transactionService: TransactionValidationAlg = transactionValidation
      def amountService: AmountValidationAlg           = amountValidation
      def paymentRepoService: PaymentRepositoryAlg[A]  = paymentRepo
    }
}
