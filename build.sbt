name := "AkkaStreams"

version := "1.0"

scalaVersion := "2.11.8"



libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.3" % "test",
  "com.typesafe.play" %% "play" % "2.5.0",
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "com.typesafe" % "config" % "1.4.0",


  "org.scalactic" %% "scalactic" % "3.1.0",
  "org.scalatest" %% "scalatest" % "3.1.0" % "test"
)


