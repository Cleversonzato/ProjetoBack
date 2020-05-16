name := """back"""
organization := "br.psi.av"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

import play.sbt.routes.RoutesKeys
RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.18.7-play27"
libraryDependencies += "org.reactivemongo" %% "reactivemongo-play-json" % "0.18.7-play27"

libraryDependencies +=  "org.mindrot" % "jbcrypt" % "0.3m"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "br.psi.av.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "br.psi.av.binders._"
