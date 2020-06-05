package com.payments

import com.payments.http.PaymentsEndpoint
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import com.payments.programs.PaymentCreation

object Runner extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(PaymentsEndpoint(PaymentCreation.build).service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
