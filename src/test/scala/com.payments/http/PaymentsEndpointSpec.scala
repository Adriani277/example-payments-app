package com.payments.http

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import io.circe._
import cats.effect._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import com.payments.http.views.PaymentView
import com.payments.algebras.PaymentCreationAlg
import com.payments.domain.{Payment, ServiceError}
import cats.implicits._

final class PaymentsEndpointSpec extends AnyFunSpec with Matchers {
  describe("payment/create endpoint") {
    it("echo's the received payload") {
      implicit val ed = jsonOf[IO, PaymentView]

      val testProgram = new PaymentCreationAlg{
        def create(payment: Payment): IO[Either[ServiceError,Payment]] = IO.pure(payment.asRight)
      }

      val json = Json.obj(
        ("name", Json.fromString("John Doe")),
        ("amount", Json.fromDoubleOrNull(42)),
        ("recipient", Json.fromString("James Doe"))
      )

      val response: IO[Response[IO]] = PaymentsEndpoint(testProgram).service.run(
        Request(method = Method.POST, uri = uri"/payment/create")
        .withEntity(json)
      )

      check[PaymentView](response, Status.Ok, Some(PaymentView("John Doe", 42d, "James Doe")))
    }
  }
}
