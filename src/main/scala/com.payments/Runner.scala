package com.payments

import com.payments.http.PaymentsEndpoint
import com.payments.interpreters._
import com.payments.programs.PaymentCreation
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder

object Runner extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val services = Services.build(AmountValidation, TransactionValidation)
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(PaymentsEndpoint(PaymentCreation.build(services)).service)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
