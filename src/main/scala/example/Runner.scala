package example

import example.http.PaymentsEndpoint
import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger

object Runner extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    val log = for {
      logger <- Slf4jLogger.create[IO]
      _      <- logger.info("A log")
    } yield ()
    log *>
      BlazeServerBuilder[IO]
        .bindHttp(8080, "localhost")
        .withHttpApp(PaymentsEndpoint.service)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
  }
}
