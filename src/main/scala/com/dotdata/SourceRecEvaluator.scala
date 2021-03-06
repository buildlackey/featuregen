package com.dotdata

import java.util.Date

import scala.collection.mutable.ArrayBuffer



// TODO - sort the list of sourceDates and do a binary search for speed.
//
case class SourceRecEvaluator(targetDates: Array[Date], ranges: List[Short]) {

  // TODO - consider storing dates as Long instead of Date (check: any other overhead for each Date than the Long?)
  //
  def isInRange(rec: SourceRec, targetRecDate: Date, range: Short): Boolean = {
    val rangeInSeconds = range * 60 * 60 * 24 /* secs/min * min/hr * hrs/day */
    val diff = ( targetRecDate.getTime - rec.date.getTime ) / 1000
    0 < diff  && diff <=  rangeInSeconds
  }

  def findRanges(rec: SourceRec): List[Short] = {
    ranges.filter{ range =>
      targetDates.exists{ date => isInRange(rec, date, range)
      }
    }
  }

  def findSubsumingTargetDates(rec: SourceRec): List[(Date,Short)] = {
    val pairs = ArrayBuffer[(Date,Short)]()


    //val startIndex = searcher.gre



    targetDates.foreach{ td =>
      ranges.foreach{ r =>
        if (isInRange(rec, td, r)) {
          pairs.append((td,r))
        }
      }
    }
    pairs.toList
  }
}
