package com.dotdata

import java.util.Date

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable._

class OutputFeatureStoreTest extends AnyFunSpec with Matchers {
  import com.dotdata.DateFormats._

  describe("OutputFeatureStore") {
    it("produces a schema with no feature fields if fed no data") {
      val store =  new OuputFeatureStore()
      store.schema shouldEqual Seq("date")
    }
    it("correctly sorts by date, feature and range") {
      val store =  new OuputFeatureStore()

      store.addEntry(dateFormat.parse("1970/01/02"), 3, 200)
      store.addEntry(dateFormat.parse("1970/01/02"), 2, 100)
      store.addEntry(dateFormat.parse("1970/01/02"), 1, 200)
      store.addEntry(dateFormat.parse("1970/01/02"), 2, 200)
      store.addEntry(dateFormat.parse("1970/01/02"), 2, 100)

      store.addEntry(dateFormat.parse("1970/01/01"), 2, 100)

      store.addEntry(dateFormat.parse("1970/01/03"), 2, 200)

      val sorted = store.sortByDateFeatureAndRange()
      val keys = sorted.keys.toList
      keys(0).toString.contains("Jan 01") shouldBe true
      keys(1).toString.contains("Jan 02") shouldBe true
      keys(2).toString.contains("Jan 03") shouldBe true

      val featuresForEachDate: Seq[scala.Seq[((Short, Short), Int)]] = sorted.values.toList.map(_.toList)
      val expected = List(
              List(
                  ((100,2),1)),
              List(
                  ((100,2),2),
                  ((200,1),1),
                  ((200,2),1),
                  ((200,3),1)),
              List(((200,2),1))
      )
      
      featuresForEachDate shouldEqual expected
    }
  }
}
