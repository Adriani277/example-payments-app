package com.payments.programs

import com.payments.algebras.PaymentUpdateAlg
import com.payments.algebras.ServicesAlg
import com.payments.domain._
import com.payments.domain.db.PaymentData
import cats.effect.IO
import cats.implicits._

final case class PaymentUpdate private (private val services: ServicesAlg[PaymentData])
    extends PaymentUpdateAlg {
  def update(amountUpdate: AmountUpdate): IO[Either[ServiceError, PaymentData]] = {
    val updatedPayment: Either[ServiceError, IO[PaymentData]] =
      services.amountService
        .validate(amountUpdate.amount)
        .map(_ => services.paymentRepoService.update(amountUpdate))

    updatedPayment.sequence
  }

}
