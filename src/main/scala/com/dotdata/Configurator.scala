package com.dotdata


import com.typesafe.config._

import scala.collection.immutable._


case class Configuration(eventIds: Seq[Int], ranges: Seq[Int])

object Configuration{

  def apply(eventIds: Seq[Int], ranges: Seq[Int], maxEventIds: Int, maxRanges: Int): Configuration = {
    if (eventIds.length > maxEventIds)
      throw new IllegalArgumentException(
        s"configured number of eventIds (${eventIds.length}) > allowed max ($maxEventIds)")
    if (ranges.length > maxEventIds)
      throw new IllegalArgumentException(
        s"configured number of ranges (${ranges.length}) > allowed max ($maxRanges)")
    Configuration(eventIds, ranges)
  }
}


object Configurator extends App {
  def toIntList(line: String): Seq[Int] = line.split(",").toList.map(_.toInt)

  def loadConfig()  = {
    val config = ConfigFactory.load("featuregen.conf")
    val maxEventIds= config.getInt("limits.maxEventIds")
    val maxRanges = config.getInt("limits.maxRanges ")
    val eventIds = toIntList( config.getString("eventIds"))
    val ranges = toIntList(config.getString("ranges"))

    Configuration(eventIds, ranges, maxEventIds, maxRanges)
  }
}
