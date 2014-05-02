name := "langbot"

version := "1.0"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "pircbot" % "pircbot" % "1.5.0",
  "org.jruby" % "jruby-complete" % "1.7.12",
  "org.python" % "jython" % "2.5.3",
  "org.clojure" % "clojure" % "1.6.0",
  "com.typesafe" % "config" % "1.2.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.jsoup" % "jsoup" % "1.7.3",
  "org.scalatest" % "scalatest_2.11" % "2.1.5" % "test"
)
