package example

import org.scalatest.matchers.should.Matchers
import org.scalatest.funspec.AnyFunSpec

class ClassNameSpec extends AnyFunSpec with Matchers {
  describe("Name Of Method") {
    it("Desired Behaviour") {
      10 shouldBe 10
    }
  }
}
