package com.payments.http.views

import io.circe.generic.semiauto._
import io.circe.Encoder
import io.circe.Decoder
import com.payments.domain._

final case class PaymentView private (name: String, amount: Double, recipient: String)
object PaymentView {
  implicit val encoder: Encoder[PaymentView] = deriveEncoder
  implicit val decoder: Decoder[PaymentView] = deriveDecoder

  def toPayment(view: PaymentView): Payment =
    Payment(Name(view.name), Amount(view.amount), Recipient(view.recipient))
}
