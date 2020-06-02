package com.payments

import org.http4s.{EntityDecoder, Status, Response}
import cats.effect.IO

package object http {
  def check[A](actual: IO[Response[IO]], expectedStatus: Status, expectedBody: Option[A])(implicit
      ev: EntityDecoder[IO, A]
  ): Boolean = {
    val actualResp  = actual.unsafeRunSync
    val statusCheck = actualResp.status == expectedStatus
    val bodyCheck = expectedBody.fold[Boolean](
      actualResp.body.compile.toVector.unsafeRunSync.isEmpty
    )(expected => actualResp.as[A].unsafeRunSync == expected)
    statusCheck && bodyCheck
  }
}
