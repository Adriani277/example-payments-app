package com.payments.http

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import com.payments.http.views._
import io.circe.syntax._
import com.payments.algebras.PaymentCreationAlg

final case class PaymentsEndpoint(creationProgram: PaymentCreationAlg) {
  val service = HttpRoutes
    .of[IO] {
      case GET -> Root / "status" =>
        Ok("""{"status" : "ok"}""")
      case r @ POST -> Root / "payment" / "create" =>
        implicit val ed = jsonOf[IO, PaymentView]
        for {
          view   <- r.as[PaymentView]
          result <- creationProgram.create(PaymentView.toPayment(view))
          response <- result match {
            case Left(e)  => UnprocessableEntity(ErrorResponse.fromServiceError(e).asJson)
            case Right(v) => Ok(PaymentCreationData.fromPayment(v).asJson)
          }
        } yield response
    }
    .orNotFound
}
