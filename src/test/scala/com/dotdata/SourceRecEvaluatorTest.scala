package com.dotdata

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.immutable._

class SourceRecEvaluatorTest extends AnyFunSpec with Matchers {
  import com.dotdata.DateFormats._

  describe("TargetRecEvaluator") {

    it("works correctly if multiple ranges match") {
      val targetDates = List(
        dateFormat.parse("1970/01/02"),
        dateFormat.parse("1970/01/03"),
        dateFormat.parse("1970/01/04")
      )

      val evaluator = SourceRecEvaluator(targetDates, List(1,2))
      val sourceRec = SourceRec(dateTimeFormat.parse("1970/01/01 00:00:00"), 100)

    }


    it("returns isInRange == true  if range is 1 day and target date is exactly 24 hours ahead of source date") {
      val targetDates = List( dateFormat.parse("1970/01/02") )
      val evaluator = SourceRecEvaluator(targetDates, List(1))
      val sourceRec = SourceRec(dateTimeFormat.parse("1970/01/01 00:00:00"), 100)
      val inRange = evaluator.isInRange(sourceRec, targetDates.head, 1)

      inRange shouldBe true
    }

    it("returns isInRange == true  if range is 1 day and target date is one second ahead of source date") {
      val targetDates = List( dateFormat.parse("1970/01/02") )
      val evaluator = SourceRecEvaluator(targetDates, List(1))
      val sourceRec = SourceRec(dateTimeFormat.parse("1970/01/01 23:59:59"), 100)
      val inRange = evaluator.isInRange(sourceRec, targetDates.head, 1)

      inRange shouldBe true
    }

    it("returns isInRange == false  if range is 1 day and target date == source date") {
      val targetDates = List( dateFormat.parse("1970/01/02") )
      val evaluator = SourceRecEvaluator(targetDates, List(1))
      val sourceRec = SourceRec(dateTimeFormat.parse("1970/01/02 00:00:00"), 100)
      val inRange = evaluator.isInRange(sourceRec, targetDates.head, 1)

      inRange shouldBe false
    }
  }
}
  //val dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd")
  //val dateTimeFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
