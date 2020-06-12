package com.payments.algebras

import com.payments.domain.{AmountUpdate, ServiceError}
import com.payments.domain.db.PaymentData
import cats.effect.IO

trait PaymentUpdateAlg {
  def update(amount: AmountUpdate): IO[Either[ServiceError, PaymentData]]
}
