package com.dotdata

import java.util.Date

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable._

// TODO - tests assume current time zone is PST.  Should set this explicitly
//
class InputProviderTest extends AnyFunSpec with Matchers {
  describe("InputProvider") {
    it("correctly parses source table") {
      val expected =
     """Sun Dec 01 12:00:00 PST 2019
       |Mon Dec 02 13:00:00 PST 2019
       |Tue Dec 03 15:00:00 PST 2019
       |Tue Dec 03 15:00:00 PST 2019
       |Wed Dec 04 10:00:00 PST 2019
       |Thu Dec 05 17:00:00 PST 2019""".stripMargin

      val dates = new InputProvider().getSourceRecs.map(_.date).mkString("\n")
      dates shouldEqual expected
    }

    it("correctly parses target table") {
      val expected =
        """Tue Dec 03 00:00:00 PST 2019
          |Wed Dec 04 00:00:00 PST 2019
          |Thu Dec 05 00:00:00 PST 2019""".stripMargin

      val dates: Array[Date] = new InputProvider().getTargetRecDates
      dates.mkString("\n") shouldEqual expected
    }

    it("correctly filters source table") {
      val expected =
        """Sun Dec 01 12:00:00 PST 2019
          |Mon Dec 02 13:00:00 PST 2019
          |Tue Dec 03 15:00:00 PST 2019
          |Thu Dec 05 17:00:00 PST 2019""".stripMargin
      val dates = new InputProvider().getFilteredSourceRecs .map(_.date).mkString("\n")
      dates shouldEqual expected
    }

    it("correctly parses range set file") {
      val expected = "1\n3"
      val dates = new InputProvider().getRanges.mkString("\n")
      dates shouldEqual expected
    }

    it("correctly parses event id set file") {
      val expected = "1\n2"
      val dates = new InputProvider().getEventIds.mkString("\n")
      dates shouldEqual expected
    }
  }
}
