package com.payments

import com.payments.http.PaymentsEndpoint
import com.payments.interpreters._
import com.payments.programs.PaymentCreation
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import com.payments.domain.Payment
import pureconfig.ConfigSource
import doobie.util.ExecutionContexts

object Runner extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    dependencies.use {
      case (_, transactor) =>
        val paymentRepo = PaymentRepository(transactor)
        val services    = Services.build[Payment](AmountValidation, TransactionValidation, paymentRepo)
        BlazeServerBuilder[IO]
          .bindHttp(8080, "localhost")
          .withHttpApp(PaymentsEndpoint(PaymentCreation.build(services)).service)
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
    }

  val dependencies = for {
    blocker    <- Blocker[IO]
    config     <- Config.make(ConfigSource.default, blocker)
    ec         <- ExecutionContexts.fixedThreadPool[IO](config.doobie.connectionPoolSize)
    transactor <- DoobieUtils.buildTransactor(config.doobie, ec, blocker)
  } yield (config, transactor)
}
