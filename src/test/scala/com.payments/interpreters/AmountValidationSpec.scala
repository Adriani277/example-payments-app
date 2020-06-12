package com.payments.interpreters

import com.payments.TestSuite
import com.payments.domain.InvalidAmountError
import com.payments.domain.Amount
import org.scalacheck.Gen

final class AmountValidationSpec extends TestSuite {
  describe("validate") {
    it("returns InvalidAmountError when amount is <= 0") {
      val gen = Gen.chooseNum(Double.MinValue, 0d).map(a => Amount(BigDecimal(a)))
      forAll(gen) { amount =>
        AmountValidation.validate(amount) shouldBe (Left(InvalidAmountError(amount)))
      }
    }

    it("returns unit when amount is greater than 0") {
      val gen =
        Gen.chooseNum(Double.MinPositiveValue, Double.MaxValue).map(a => Amount(BigDecimal(a)))
      forAll(gen) { amount =>
        AmountValidation.validate(amount) shouldBe (Right(()))
      }
    }
  }
}
