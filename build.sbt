ThisBuild / organization := "com.bheaver.nglx"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / version := "0.1.0-SNAPSHOT"

val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.4.0"
val MongodbDriverVersion = "4.8.1"
val TypeSafeConfigVersion = "1.4.2"
val ScalaTestVersion = "3.2.15"
val ScalaTestFlatSpecVersion = "3.2.15"
val ScalaMockVersion = "5.1.0"

lazy val root = (project in file(".")).aggregate(core, protocol)


lazy val core = (project in file("core")).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "org.mongodb.scala" %% "mongo-scala-driver" % MongodbDriverVersion,
    "com.typesafe" % "config" % TypeSafeConfigVersion,
    "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
    "org.scalatest" %% "scalatest-flatspec" % ScalaTestFlatSpecVersion % "test",
    "org.scalamock" %% "scalamock" % ScalaMockVersion % Test
  )
).dependsOn(protocol)

lazy val protocol = (project in file("protocol"))