package com.payments

import com.payments.http.PaymentsEndpoint
import com.payments.interpreters._
import com.payments.interpreters.db._
import com.payments.programs.PaymentCreation
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigSource
import doobie.util.ExecutionContexts
import com.payments.programs.PaymentUpdate
import com.payments.domain.db.PaymentData
import doobie.util.transactor.Transactor
import com.payments.Config.ServerConfig

object Runner extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    dependencies.use {
      case (config, transactor) =>
        val paymentRepo = PaymentRepository(transactor)
        val services =
          Services.build[PaymentData](AmountValidation, TransactionValidation, paymentRepo)

        BlazeServerBuilder[IO]
          .bindHttp(config.port, config.host)
          .withHttpApp(PaymentsEndpoint(PaymentCreation(services), PaymentUpdate(services)).service)
          .serve
          .compile
          .drain
          .as(ExitCode.Success)
    }

  val dependencies: Resource[IO, (ServerConfig, Transactor[IO])] = for {
    blocker    <- Blocker[IO]
    config     <- Config.make(ConfigSource.default, blocker)
    ec         <- ExecutionContexts.fixedThreadPool[IO](config.doobie.connectionPoolSize)
    transactor <- DoobieUtils.buildTransactor(config.doobie, ec, blocker)
  } yield (config.server, transactor)
}
