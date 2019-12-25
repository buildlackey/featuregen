package com.dotdata

import java.util.Date

import com.typesafe.scalalogging.LazyLogging

import scala.collection.immutable.ListMap
import scala.collection.mutable

class OuputFeatureStore extends LazyLogging {

  private def eventIdRangePairToCountDefault  = // new instance of default map each time
    mutable.Map[(Short,Short),Int]().withDefaultValue(0)

  private val datesToEventIdRangeCounts =
    mutable.Map[Date,mutable.Map[(Short,Short),Int]]().withDefault(_ => eventIdRangePairToCountDefault)


  def addEntry(date: Date, range: Short, eventId: Short): Unit = {
    logger.info(s"addEntry: $date, $range, $eventId")
    val pairToCountMap: mutable.Map[(Short, Short), Int] = datesToEventIdRangeCounts (date)    //drop type

    val pair = (eventId, range)
    val count = pairToCountMap(pair)

    pairToCountMap.update(pair, count + 1)



    println(s"before add of $date" + datesToEventIdRangeCounts)
    datesToEventIdRangeCounts.update(date, pairToCountMap)
    println(s"after add of $date" + datesToEventIdRangeCounts)

  }


  // Sorts datesToEventIdRangeCounts by date, then for each date, sorts the mapping of eventId/range pairs to counts
  // using the pair as a sort key
  //
  def sortByDateFeatureAndRange(): Map[Date, Seq[((Short, Short), Int)]] = {
    val sortedMapEntries: Seq[(Date, mutable.Map[(Short, Short), Int])] = datesToEventIdRangeCounts.toSeq.sortBy(_._1)
    System.out.println("sortedMapEntries:" + sortedMapEntries);

    val sortedMap: Map[Date, mutable.Map[(Short, Short), Int]] = ListMap(sortedMapEntries:_*)

    sortedMap.map { case (date, eventIdRangeToCount) =>
      val sortedByEventIdRange: Seq[((Short, Short), Int)] = eventIdRangeToCount.toSeq.sortBy(_._1)
      logger.debug(s"sorted $eventIdRangeToCount to $sortedByEventIdRange")
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
