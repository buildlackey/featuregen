package com.dotdata

import com.typesafe.scalalogging.LazyLogging

/**
  *
  *
  *
  *  Engineering principles we seek to demonstrate
  *
  *   - emit log messages at various levels to simplify debugging
  *
  *   - configurability through typesafe config
  *
  */

object FeatureGen extends App with LazyLogging {


  val config = Configurator.loadConfig


  println("config:" + config);

}
