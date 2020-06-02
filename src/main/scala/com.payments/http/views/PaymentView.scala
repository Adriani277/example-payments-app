package com.payments.http.views

import io.circe.generic.semiauto._
import io.circe.Encoder
import io.circe.Decoder

final case class PaymentView private (name: String, amount: Double, recipient: String)
object PaymentView {
  implicit val encoder: Encoder[PaymentView] = deriveEncoder
  implicit val decoder: Decoder[PaymentView] = deriveDecoder
}
