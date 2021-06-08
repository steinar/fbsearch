scalaVersion := "2.13.3"
name := "fbsearch"
organization := "is.steinar"
version := "1.0"

val circeVersion = "0.14.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-generic-extras"
).map(_ % circeVersion)

scalacOptions += "-Ymacro-annotations"
