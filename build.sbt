
name := "twframe"

organization := "com.twframe"

version := "0.0.1"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.8"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV
  )
}

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "0.3-SNAPSHOT"
)