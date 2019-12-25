package com.dotdata

import scopt.OParser
import java.io.File


case class Opts(eventIds: Seq[Int], rangeSet: Seq[Int])

class CmdLineOpts { // mine

}

case class Config(
                   foo: Int = -1,
                   out: File = new File("."),
                   xyz: Boolean = false,
                   libName: String = "",
                   maxCount: Int = -1,
                   verbose: Boolean = false,
                   debug: Boolean = false,
                   mode: String = "",
                   files: Seq[File] = Seq(),
                   keepalive: Boolean = false,
                   jars: Seq[File] = Seq(),
                   kwargs: Map[String, String] = Map())


object OptsParser extends App {

  import scopt.OParser

  val builder = OParser.builder[Config]
  val parser1: OParser[Unit, Config] = {
    import builder._
    OParser.sequence(
      programName("FeatureGen"),
      head("scopt", "4.x"),
      // option -f, --foo
      opt[Int]('f', "foo")
        .action((x, c) => c.copy(foo = x))
        .text("foo is an integer property")
    )
  }

  // OParser.parse returns Option[Config]
  OParser.parse(parser1, args, Config()) match {
    case Some(config) =>

      println(s"the config is: $config")
    case _ =>
    // arguments are bad, error message will have been displayed
  }

}


