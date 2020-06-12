package com.payments.algebras

import com.payments.domain._
import com.payments.domain.db.PaymentData
import cats.effect.IO

trait PaymentCreationAlg {
  def create(payment: Payment): IO[Either[ServiceError, PaymentData]]
}
