name := "RainDanceBot"

version := "0.1"

scalaVersion := "2.12.4"

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.iheart" %% "ficus" % "1.4.3",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "joda-time" % "joda-time" % "2.9.9",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.7", // Must be 2.8.7 to support Discord4J... Upgrade if it upgrades
  "com.github.austinv11" % "Discord4J" % "2.9.2",
  "com.jsuereth" %% "scala-arm" % "2.0",
  "io.spray" %% "spray-json" % "1.3.3",
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
  "org.specs2" %% "specs2-mock" % "4.0.2" % Test
)

