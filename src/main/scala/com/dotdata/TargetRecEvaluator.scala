package com.dotdata

import java.util.Date



object RangeChecker{


  // TODO - we will need to change this once we have dates stored as Longs
  // TODO - test equality (same date)

  def isInRange(rec: SourceRec, sourceDate: Date, range: Short): Boolean = {
    val rangeInSeconds = range * 60 * 60 * 24 /* secs/min * min/hr * hrs/day */
    val diff = sourceDate.getTime() - rec.date.getTime()
    0 < diff  && diff <  rangeInSeconds
  }


}


// TODO - sort the list of sourcerDates and do a binary search for speed.
//
case class TargetRecEvaluator(sourceDates: List[Date], ranges: List[Short]) {
  import RangeChecker._

  def findRanges(rec: SourceRec): List[Short] = {
    ranges.filter{ range =>
      sourceDates.exists{date => isInRange(rec, date, range)
      }
    }
  }
}
