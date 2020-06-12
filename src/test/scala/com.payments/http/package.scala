package com.payments

import org.http4s.{EntityDecoder, Status, Response}
import cats.effect.IO
import cats.implicits._
import cats.kernel.Eq
import org.scalatest.compatible.Assertion
import org.scalatest.Assertions

package object http {
  def check[A: Eq](actual: IO[Response[IO]], expectedStatus: Status, expectedBody: Option[A])(
      implicit ev: EntityDecoder[IO, A]
  ): Assertion = {
    val actualResp  = actual.unsafeRunSync
    val statusCheck = actualResp.status === expectedStatus
    val bodyCheck = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty
    )(expected => actualResp.as[A].unsafeRunSync === expected)
    Assertions.assert(statusCheck && bodyCheck)
  }
}
