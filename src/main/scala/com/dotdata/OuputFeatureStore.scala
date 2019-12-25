package com.dotdata

import java.util.Date

import com.typesafe.scalalogging.LazyLogging

import scala.collection.immutable.ListMap
import scala.collection.mutable

object OuputFeatureStore extends LazyLogging {

  private val eventIdRangePairToCountDefault  =
    mutable.Map[(Short,Short),Int]().withDefaultValue(0)

  private val datesToEventIdRangeCounts =
    mutable.Map[Date,mutable.Map[(Short,Short),Int]]().withDefaultValue(eventIdRangePairToCountDefault)


  def addEntry(date: Date, range: Short, eventId: Short): mutable.Map[Date, mutable.Map[(Short, Short), Int]] = {
    logger.info(s"addEntry: $date, $range, $eventId")
    val pairToCountMap: mutable.Map[(Short, Short), Int] = datesToEventIdRangeCounts (date)    //drop type
    val pair = (eventId, range)
    val count = pairToCountMap(pair)

    pairToCountMap.updated(pair, count + 1)
    datesToEventIdRangeCounts.updated(date, pairToCountMap)
  }


  // Sorts datesToEventIdRangeCounts by date, then for each date, sorts the mapping of eventId/range pairs to counts
  // using the pair as a sort key
  //
  def sortByDateFeatureAndRange(): Map[Date, Seq[((Short, Short), Int)]] = {
    val sortedMapEntries: Seq[(Date, mutable.Map[(Short, Short), Int])] = datesToEventIdRangeCounts.toSeq.sortBy(_._1)
    val sortedMap: Map[Date, mutable.Map[(Short, Short), Int]] = ListMap(sortedMapEntries:_*)

    sortedMap.map { case (date, eventIdRangeToCount) =>
      val sortedByEventIdRange: Seq[((Short, Short), Int)] = eventIdRangeToCount.toSeq.sortBy(_._1)
      (date, sortedByEventIdRange)
    }
  }


  // Returns schema that will be used for first line of output features file
  // Sorts dateToFeatureMap by date, then converts the entry for each date into an output string of the form
  // <date>,feature(<featid>,<range>),feature(<featid>,<range>)
  //
  def schema: Seq[String] = {
    val featureFields = sortByDateFeatureAndRange().toList.map {
      case (date, pairs: Seq[((Short, Short), Int)]) =>
        pairs.map(_._1).map {
          case (feature: Short, range: Short) =>
            s"feature($feature,$range)"
        }.mkString(",")
    }

    "date" :: featureFields
  }

  // Returns printable representation of each line  of output features that will be printed after the header
  //
  def toPrintableRep: Seq[String] = {
    import DateFormats._

    sortByDateFeatureAndRange().toSeq.map {
      case (date, pairs) =>
        val printableFeatureRangePairs = pairs.map(_._1).map {
          case (feature: Short, range: Short) =>
            s"feature($feature,$range)"
        }.mkString(",")

        s"${dateFormat.format(date)},$printableFeatureRangePairs}"
    }
  }
}
