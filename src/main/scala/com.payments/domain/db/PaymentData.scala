package com.payments.domain.db

import java.util.UUID
import com.payments.domain._

final case class PaymentData(id: UUID, name: Name, amount: Amount, recipient: Recipient)
