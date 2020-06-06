package com.payments.interpreters

import com.payments.TestSuite
import com.payments.domain._
import org.scalacheck.Gen
import cats.implicits._

class TransactionValidationSpec extends TestSuite {
  describe("validate") {
    it("returns InvalidTransactionError when name and recipient are equal") {
      val gen =
        for {
          name <- Gen.alphaNumStr
        } yield (Name(name), Recipient(name))

      forAll(gen) {
        case (name, recipient) =>
          TransactionValidation.validate(name, recipient) shouldBe (Left(InvalidTransactionError))
      }
    }

    it("returns unit when name and recipient are not equal") {
      val gen =
        for {
          name      <- Gen.alphaNumStr
          recipient <- Gen.alphaNumStr.withFilter(_ =!= name)
        } yield (Name(name), Recipient(recipient))

      forAll(gen) {
        case (name, recipient) =>
          TransactionValidation.validate(name, recipient) shouldBe (Right(()))
      }
    }
  }
}
