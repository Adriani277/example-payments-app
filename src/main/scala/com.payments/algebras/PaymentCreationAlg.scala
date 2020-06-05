package com.payments.algebras

import com.payments.domain._
import cats.effect.IO

trait PaymentCreationAlg {
  def create(payment: Payment): IO[Either[ServiceError, Payment]]
}
