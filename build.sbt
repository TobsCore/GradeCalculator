name := "GradeCalculator"
version := "0.1"
scalaVersion := "2.12.6"

test in assembly := {}

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11"
libraryDependencies += "org.scalafx" %% "scalafxml-core-sfx8" % "0.4"
libraryDependencies += "eu.lestard" % "advanced-bindings" % "0.4.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"


// Includes the JavaFX Stylesheet, when SBT is run from the terminal
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

// Needed in order to make scalamacros work
addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)

// Allows the process to be restarted, when run from sbt
fork in run := true

mainClass in Compile := Some("tobscore.sideprojects.gradecalc.GradeCalculator")
mainClass in assembly := Some("tobscore.sideprojects.gradecalc.GradeCalculator")
