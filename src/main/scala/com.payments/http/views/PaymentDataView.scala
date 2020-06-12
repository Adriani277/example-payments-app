package com.payments.http.views

import java.util.UUID

import io.circe.generic.semiauto._
import com.payments.domain.db.PaymentData
import io.circe.Codec

final case class PaymentDataView(
    id: UUID,
    name: String,
    amount: BigDecimal,
    recipient: String
)
object PaymentDataView {
  implicit val encoder: Codec[PaymentDataView] = deriveCodec[PaymentDataView]

  def fromPaymentData(payment: PaymentData): PaymentDataView =
    PaymentDataView(payment.id, payment.name.value, payment.amount.value, payment.recipient.value)
}
