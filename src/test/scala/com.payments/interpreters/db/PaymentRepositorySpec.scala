package com.payments.interpreters.db

import java.util.UUID

import com.payments.TestSuite
import doobie.util.transactor.Transactor
import cats.effect.IO

final class PaymentRepositorySpec extends TestSuite with doobie.scalatest.IOChecker {
  implicit val cs = IO.contextShift(scala.concurrent.ExecutionContext.global)

  val transactor = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver",
    "jdbc:mysql://localhost/example_payments_app?useSSL=false",
    "root",
    ""
  )

  describe("select") {
    it("produces valid query with correct types") {
      checkOutput(PaymentRepository.select(UUID.randomUUID))
    }
  }
}
