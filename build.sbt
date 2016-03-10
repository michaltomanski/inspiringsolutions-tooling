name := """Twit Fetcher (InspiringSolutins)"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.4.2" % Test,
  "com.typesafe.akka" %% "akka-actor" % "2.4.2",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.2",
  "com.typesafe.akka" %% "akka-stream" % "2.4.2",
  "com.hunorkovacs" %% "koauth" % "1.1.0",
  "org.json4s" %% "json4s-native" % "3.3.0"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

herokuAppName in Compile := "peaceful-ridge-14628"

routesGenerator := InjectedRoutesGenerator
