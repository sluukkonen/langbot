name := "langbot"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "pircbot" % "pircbot" % "1.5.0",
  "org.jruby" % "jruby-complete" % "9.0.0.0",
  "org.python" % "jython" % "2.5.3",
  "org.clojure" % "clojure" % "1.7.0",
  "com.typesafe" % "config" % "1.3.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "org.jsoup" % "jsoup" % "1.8.2",
  "org.scala-lang" % "scala-compiler" % "2.11.7",
  "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test",
  "log4j" % "log4j" % "1.2.17"
)

fork in test := true
