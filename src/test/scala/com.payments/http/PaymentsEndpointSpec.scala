package com.payments.http

import com.payments.algebras.PaymentCreationAlg
import com.payments.domain._
import com.payments.TestSuite
import com.payments.algebras.PaymentUpdateAlg
import com.payments.domain.db.PaymentData
import io.circe.literal._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import cats.implicits._
import org.scalacheck.Gen
import com.payments.http.views.PaymentDataView
import cats.kernel.Eq

final class PaymentsEndpointSpec extends TestSuite {
  import PaymentsEndpointSpec._

  describe("payment/create endpoint") {
    it("returns a PaymentDataView on success") {
      forAll(gen) {
        case (id, name, amount, recipient) =>
          implicit val ed = jsonOf[IO, PaymentDataView]

          val testCreationProgram = new TestCreationProgram {
            override def create(payment: Payment): IO[Either[ServiceError, PaymentData]] =
              IO.pure(PaymentData(id, payment.name, payment.amount, payment.recipient).asRight)
          }

          val testUpdateProgram = new TestUpdateProgram

          val json =
            json"""
            {
              "name" : $name,
              "amount" : $amount,
              "recipient" : $recipient
            }
            """

          val response: IO[Response[IO]] =
            PaymentsEndpoint(testCreationProgram, testUpdateProgram).service.run(
              Request(method = Method.POST, uri = uri"/payment/create")
                .withEntity(json)
            )

          check[PaymentDataView](
            response,
            Status.Created,
            Some(PaymentDataView(id, name, amount, recipient))
          )
      }
    }
  }

  describe("payment/update endpoint") {
    it("returns a PaymentDataView on success") {
      forAll(gen) {
        case (id, name, amount, recipient) =>
          implicit val ed = jsonOf[IO, PaymentDataView]

          val testCreationProgram = new TestCreationProgram
          val testUpdateProgram = new TestUpdateProgram {
            override def update(amount: AmountUpdate): IO[Either[ServiceError, PaymentData]] =
              IO.pure(
                PaymentData(amount.id, Name(name), amount.amount, Recipient(recipient)).asRight
              )
          }

          val json =
            json"""
            {
              "id" : $id,
              "amount" : $amount
            }
            """

          val response: IO[Response[IO]] =
            PaymentsEndpoint(testCreationProgram, testUpdateProgram).service.run(
              Request(method = Method.PUT, uri = uri"/payment/update")
                .withEntity(json)
            )

          check[PaymentDataView](
            response,
            Status.Ok,
            Some(PaymentDataView(id, name, amount, recipient))
          )
      }
    }
  }
}

object PaymentsEndpointSpec {
  implicit val paymentDataViewEq: Eq[PaymentDataView] = Eq.fromUniversalEquals[PaymentDataView]

  val gen = for {
    id        <- Gen.uuid
    name      <- Gen.alphaNumStr
    amount    <- Gen.chooseNum(Double.MinValue, Double.MaxValue)
    recipient <- Gen.alphaNumStr
  } yield (id, name, amount, recipient)

  class TestCreationProgram extends PaymentCreationAlg {
    def create(payment: Payment): IO[Either[ServiceError, PaymentData]] = ???
  }

  class TestUpdateProgram extends PaymentUpdateAlg {
    def update(amount: AmountUpdate): IO[Either[ServiceError, PaymentData]] = ???
  }
}
