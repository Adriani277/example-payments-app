package com.payments

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckDrivenPropertyChecks

trait TestSuite extends AnyFunSpec with Matchers with ScalaCheckDrivenPropertyChecks
