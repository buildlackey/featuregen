package com.dotdata

import java.util.Date

import scala.collection.immutable.ListMap
import scala.collection.mutable

object OuputFeatureStore {
                         // range  /  list of events.
  private val rangeToEventIdMapDefault =
    mutable.Map[Short,mutable.Set[Short]]().withDefaultValue(mutable.Set[Short]())
  private val dateToFeatureMap =
    mutable.Map[Date,mutable.Map[Short,mutable.Set[Short]]]().withDefaultValue(rangeToEventIdMapDefault)


  def addEntry(date: Date, range: Short, eventId: Short): mutable.Map[Date, mutable.Map[Short, mutable.Set[Short]]] = {
    val rangeToEventIds: mutable.Map[Short, mutable.Set[Short]] = dateToFeatureMap(date)    //drop type
    val eventIds: mutable.Set[Short] = rangeToEventIds(range)


    eventIds.add(eventId)
    rangeToEventIds.updated(range, eventIds)
    dateToFeatureMap.updated(date, rangeToEventIds)
  }


  // Sorts dateToFeatureMap by date, then converts the entry for each date into an output string of the form
  // <date>,feature(<featid>,<range>),feature(<featid>,<range>)
  //
  def sortByDateFeatureAndRange(): Map[Date, Seq[(Short, Short)]] = {
    val sortedMapEntries: Seq[(Date, mutable.Map[Short, mutable.Set[Short]])] = dateToFeatureMap.toSeq.sortBy(_._1)
    val sortedMap: Map[Date, mutable.Map[Short, mutable.Set[Short]]] = ListMap(sortedMapEntries:_*)

    sortedMap.map { case (date, rangeToFeatures) =>
      val rangeAndFeaturePairs = rangeToFeatures.toSeq.flatMap {
        case (range, features) =>
          features.toSeq.map((_, range))
      }

      (date, rangeAndFeaturePairs)
    }
  }

  def toPrintableRep: Seq[String] = {
    import DateFormats._

    sortByDateFeatureAndRange().toSeq.map {
      case (date, pairs) =>
        val printableFeatureRangePairs = pairs.map {
          case (feature, range) =>
            s"feature($feature,$range)"
        }.mkString(",")

        s"${dateFormat.format(date)},$printableFeatureRangePairs}"
    }
  }
}
