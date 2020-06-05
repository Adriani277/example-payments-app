package com.payments.domain

sealed trait ServiceError
final case class InvalidAmountError(amount: Amount) extends ServiceError