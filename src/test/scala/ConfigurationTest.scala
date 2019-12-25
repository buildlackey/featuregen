import com.dotdata.Configuration
import org.scalatest.funspec.{AnyFunSpec, AsyncFunSpec}
import org.scalatest.matchers.should
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable._


class ConfigurationTest extends AnyFunSpec with Matchers {
  describe("Configurator") {
    it("raises error if length eventIds list is more than allowed max") {
      (the[IllegalArgumentException] thrownBy {
        Configuration(Seq(1, 2), Seq(3), 1, 1)
      } should have).
        message("configured number of eventIds (2) > allowed max (1)")
    }
    it("raises error if length of range list is more than allowed max") {
      (the[IllegalArgumentException] thrownBy {
        Configuration(Seq(1), Seq(1, 3), 1, 1)
      } should have).
        message("configured number of ranges (2) > allowed max (1)")
    }
  }
}
