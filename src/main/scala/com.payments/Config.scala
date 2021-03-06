package com.payments

import com.payments.Config._
import cats.effect.Resource
import cats.effect.IO
import cats.effect.Blocker
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import cats.effect.ContextShift

final case class Config(server: ServerConfig, doobie: DoobieConfig)

object Config {
  final case class ServerConfig(host: String, port: Int)

  final case class DoobieConfig(
      driver: String,
      url: String,
      user: String,
      password: String,
      connectionPoolSize: Int
  )

  def make(source: ConfigSource, blocker: Blocker)(implicit
      cs: ContextShift[IO]
  ): Resource[IO, Config] =
    Resource.liftF(blocker.delay[IO, Config](source.loadOrThrow[Config]))
}
