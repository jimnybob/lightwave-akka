name := "lightwave-akka"

version := "2.4.11"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.14"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  %  akkaVersion,
  "com.typesafe.akka" %% "akka-remote" %  akkaVersion,
  "org.typelevel"     %% "cats"        % "0.8.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.0",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "org.specs2" %% "specs2" % "2.3.11" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % Test
)

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))
