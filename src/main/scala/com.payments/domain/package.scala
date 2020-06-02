package com.payments

package object domain {
  final case class Name private (value: String)
  final case class Amount private (value: Double)
  final case class Recipient private (value: String)
}
