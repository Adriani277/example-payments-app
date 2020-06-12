package com.payments.http.views

import com.payments.domain._
import io.circe.Codec
import io.circe.generic.semiauto._

final case class PaymentView private (name: String, amount: Double, recipient: String)
object PaymentView {
  implicit val coded: Codec[PaymentView] = deriveCodec[PaymentView]

  def toPayment(view: PaymentView): Payment =
    Payment(Name(view.name), Amount(BigDecimal(view.amount)), Recipient(view.recipient))
}
