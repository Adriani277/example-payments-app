package com.payments.algebras

import com.payments.domain.Payment
import cats.effect.IO
import com.payments.domain.AmountUpdate

trait PaymentRepositoryAlg[A] {
  def create(payment: Payment): IO[A]
  def update(amount: AmountUpdate): IO[A]
}
