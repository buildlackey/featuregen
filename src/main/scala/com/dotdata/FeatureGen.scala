package com.dotdata

import java.io.{File, PrintWriter}

import scala.collection.immutable.{BitSet, ListMap}
import java.util.Date

import com.typesafe.scalalogging.LazyLogging

import scala.collection.{immutable, mutable}
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

/**
  * Engineering principles we seek to demonstrate
  *
  *   - conserve memory by streaming items and processing one by one rather than loading everything into memory
  *
  *   - efficient representation: use Bitsets and Shorts where possible to reduce memory consumption
  *
  *
  * Engineering principles we would_have demonstrated with a bit more time
  *
  *   - rather than keeping all of output feature store in an in-memory map it would have been better
  *     to write the entries out one at a time, then sort in slices via quick sort, then merge the slices
  *     via merge sort
  *
  *   - Instead of doing a linear search through sourceDates should have instead done a binary search for speed.
  *
  */

// TODO - in run instructions mention how to turn off logging in command line

case class SourceRec(date: Date, eventId: Short)

// Ideally these would be passed via the command line, or by some other configurable mechanism
//



// TODO: With more time we would take the output directory and maybe file name as a command line parameter
//
object FeatureGen extends App with LazyLogging {
  val outputFileName = "features.csv"
  val filteredSourceRecs: Iterator[SourceRec] = InputProvider.getFilteredSourceRecs
  val dates = InputProvider.getTargetRecDates
  val ranges = InputProvider.getRanges
  val evaluator = SourceRecEvaluator(dates, ranges)
  val uniqueEvents = InputProvider.getEventIds.toSeq
  val featureStore =  new OuputFeatureStore(ranges, uniqueEvents)

  filteredSourceRecs.foreach { rec =>
    logger.debug(s"evaluating source rec: $rec")
    val rangesWithin = evaluator.findRanges(rec)
    rangesWithin.foreach { range =>
      featureStore.addEntry(rec.date, range, rec.eventId)
    }
  }

  val writer = new PrintWriter(new File(outputFileName ))
  writer.write(featureStore.schema.mkString(","))
  featureStore.toPrintableRep.foreach{writer.write}
  writer.close()
}
