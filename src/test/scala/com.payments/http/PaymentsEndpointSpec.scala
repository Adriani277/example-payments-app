package com.payments.http

import io.circe._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import com.payments.http.views.PaymentView
import com.payments.algebras.PaymentCreationAlg
import com.payments.domain.{Payment, ServiceError}
import cats.implicits._
import org.scalacheck.Gen
import com.payments.TestSuite

final class PaymentsEndpointSpec extends TestSuite {
  describe("payment/create endpoint") {
    it("echo's the received payload") {
      val gen = for {
        name      <- Gen.alphaNumStr
        amount    <- Gen.chooseNum(Double.MinValue, Double.MaxValue)
        recipient <- Gen.alphaNumStr
      } yield (name, amount, recipient)
      forAll(gen) {
        case (name, amount, recipient) =>
          implicit val ed = jsonOf[IO, PaymentView]

          val testProgram = new PaymentCreationAlg {
            def create(payment: Payment): IO[Either[ServiceError, Payment]] =
              IO.pure(payment.asRight)
          }

          val json = Json.obj(
            ("name", Json.fromString(name)),
            ("amount", Json.fromDoubleOrNull(amount)),
            ("recipient", Json.fromString(recipient))
          )

          val response: IO[Response[IO]] = PaymentsEndpoint(testProgram).service.run(
            Request(method = Method.POST, uri = uri"/payment/create")
              .withEntity(json)
          )

          check[PaymentView](response, Status.Ok, Some(PaymentView(name, amount, recipient)))
      }
    }
  }
}
