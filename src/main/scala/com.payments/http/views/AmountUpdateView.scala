package com.payments.http.views

import java.util.UUID

import io.circe.Codec
import io.circe.generic.semiauto._
import com.payments.domain._

final case class AmountUpdateView(id: UUID, amount: Double)
object AmountUpdateView {
  implicit val coded: Codec[AmountUpdateView] = deriveCodec[AmountUpdateView]

  def toAmountUpdate(view: AmountUpdateView): AmountUpdate =
    AmountUpdate(view.id, Amount(BigDecimal(view.amount)))

}
