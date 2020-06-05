package com.payments.http.views

import io.circe.generic.semiauto._
import io.circe.Encoder
import com.payments.domain._

final case class PaymentCreationData private (name: Name, amount: Amount, recipient: Recipient)
object PaymentCreationData {
  implicit val nameEncoder                           = Encoder.encodeString.contramap[Name](_.value)
  implicit val amountEncoder                         = Encoder.encodeDouble.contramap[Amount](_.value)
  implicit val recipientEncoder                      = Encoder.encodeString.contramap[Recipient](_.value)
  implicit val encoder: Encoder[PaymentCreationData] = deriveEncoder

  def fromPayment(payment: Payment): PaymentCreationData =
    PaymentCreationData(payment.name, payment.amount, payment.recipient)
}
