package com.payments

import scala.concurrent.ExecutionContext
import cats.effect._
import doobie.hikari.HikariTransactor
import com.payments.Config.DoobieConfig

object DoobieUtils {
  def buildTransactor(
      config: DoobieConfig,
      ec: ExecutionContext,
      blocker: Blocker
  )(implicit cs: ContextShift[IO]): Resource[IO, HikariTransactor[IO]] =
    HikariTransactor.newHikariTransactor[IO](
      config.driver,
      config.url,
      config.user,
      config.password,
      ec,
      blocker
    )
}
