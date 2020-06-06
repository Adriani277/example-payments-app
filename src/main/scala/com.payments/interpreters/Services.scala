package com.payments.interpreters

import com.payments.algebras.ServicesAlg
import com.payments.algebras.AmountValidationAlg
import com.payments.algebras.TransactionValidationAlg

object Services {
  def build(
      amountValidation: AmountValidationAlg,
      transactionValidation: TransactionValidationAlg
  ): ServicesAlg =
    new ServicesAlg {
      def transactionService: TransactionValidationAlg = transactionValidation
      def amountService: AmountValidationAlg           = amountValidation
    }
}
