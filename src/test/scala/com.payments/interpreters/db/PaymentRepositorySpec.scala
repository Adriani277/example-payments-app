package com.payments.interpreters.db

import java.util.UUID

import com.payments.TestSuite
import doobie.util.transactor.Transactor
import cats.effect.IO
import org.scalacheck.Gen
import com.payments.domain._
import org.scalatest.BeforeAndAfterEach
import doobie._
import doobie.implicits._

final class PaymentRepositorySpec
    extends TestSuite
    with doobie.scalatest.IOChecker
    with BeforeAndAfterEach {
  implicit val cs = IO.contextShift(scala.concurrent.ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    "jdbc:mysql://localhost/example_payments_app?useSSL=false",
    "root",
    ""
  )

  override protected def beforeEach(): Unit = {
    sql"truncate table payments".update.run.transact(transactor)
  }

  val repo = PaymentRepository(transactor)

  describe("DB Test") {
    describe("select") {
      it("produces valid query with correct types") {
        checkOutput(PaymentRepository.select(UUID.randomUUID))
      }
    }

    describe("create") {
      it("creates a payment") {
        // Potential bug if Name or Recipient is > 45 characters
        val gen =
          for {
            name      <- Gen.listOfN(45, Gen.alphaChar).map(_.mkString)
            amount    <- Gen.chooseNum(Double.MinPositiveValue, 1000000d).map(Amount.apply)
            recipient <- Gen.listOfN(45, Gen.alphaChar).map(_.mkString)
          } yield Payment(Name(name), amount, Recipient(recipient))

        forAll(gen) { payment =>
          repo.create(payment).unsafeRunSync()
        }
      }
    }
  }
}
