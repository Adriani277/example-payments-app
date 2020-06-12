package com.payments.algebras

import com.payments.domain.Payment
import cats.effect.IO

trait PaymentRepositoryAlg[A] {
  def create(payment: Payment): IO[A]
}
