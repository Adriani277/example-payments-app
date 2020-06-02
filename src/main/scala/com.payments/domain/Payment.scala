package com.payments.domain

final case class Payment private (name: Name, amount: Amount, recipient: Recipient)
