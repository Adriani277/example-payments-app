package com.payments.http

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.circe._
import com.payments.http.views.PaymentView
import io.circe.syntax._

object PaymentsEndpoint {
  val service = HttpRoutes
    .of[IO] {
      case GET -> Root / "status" =>
        Ok("""{"status" : "ok"}""")
      case r @ POST -> Root / "payment" / "create" =>
        implicit val ed = jsonOf[IO, PaymentView]
        for {
          res      <- r.as[PaymentView]
          response <- Ok(res.asJson)
        } yield response
    }
    .orNotFound
}
