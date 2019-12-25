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
  *   - configurability through typesafe config
  *
  *
  * Engineering principles we would_have demonstrated with a bit more time
  *
  *   - rather than keeping all of output feature store in an in-memory map it would have been better
  *     to write the entries out one at a time, then sort in slices via quick sort, then merge the slices
  *     via merge sort
  *
  */


case class SourceRec(date: Date, eventId: Short)

// Ideally these would be passed via the command line, or by some other configurable mechanism
//




object FeatureGen extends App with LazyLogging {

  val outputFileName = "features.csv"
  val filteredTargetRecs: Iterator[SourceRec] = InputProvider.getFilteredSourceRecs
  val targetRecEvaluator = TargetRecEvaluator(InputProvider.getTargetRecDates, InputProvider.getRanges)

  filteredTargetRecs.foreach { rec =>
    val rangesWithin = targetRecEvaluator.findRanges(rec)
    rangesWithin.foreach { range =>
      OuputFeatureStore.addEntry(rec.date, range, rec.eventId)
    }
  }

  // With more time we would take the output directory and maybe file name as a command line parameter
  val writer = new PrintWriter(new File(outputFileName ))
  OuputFeatureStore.toPrintableRep.foreach{writer.write}
  writer.close()
}
