package com.dotdata

import java.util.Date

import scala.collection.immutable.BitSet
import scala.io.Source

object InputProvider {
  import DateFormats._

  val source = s"${System.getProperty("user.dir")}/src/main/resources/source_table.csv"
  val target = s"${System.getProperty("user.dir")}/src/main/resources/target_table.csv"
  val eventIds = s"${System.getProperty("user.dir")}/src/main/resources/event_id_set.csv"
  val ranges = s"${System.getProperty("user.dir")}/src/main/resources/range_id_set.csv"

  // Don't need the dollar amounts from the target records, just the dates to do range compare
  def getTargetRecDates: Array[Date] =
    Source.fromFile(target).getLines.
      map{ line =>
        dateFormat.parse(line.split(",")(0))
      }.toArray.sorted     // sorting these because we eventually want to use binary, not linear search


  def getRanges: List[Short] =
    Source.fromFile(ranges).getLines.map{_.toShort}.toList

  def getEventIds: BitSet = {
    val ids = Source.fromFile(eventIds).getLines.map{_.toInt}.toSeq
    BitSet(ids:_*)
  }

  def getSourceRecs: Iterator[SourceRec] =    // iterator so we can stream the list rather than load all at once
    Source.fromFile(source).getLines.
      map{ line =>
        val parts = line.split(",")
        val str = parts(0)
        SourceRec(dateTimeFormat.parse(str), parts(1).toShort)
      }

  def getFilteredSourceRecs : Iterator[SourceRec] =  {
    val eventIds = getEventIds
    getSourceRecs.filter(rec => eventIds.contains(rec.eventId))
  }
}
