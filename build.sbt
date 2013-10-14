name := "langbot"

version := "1.0"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "pircbot" % "pircbot" % "1.5.0",
  "org.jruby" % "jruby-complete" % "1.7.5",
  "org.mozilla" % "rhino" % "1.7R4",
  "org.python" % "jython" % "2.5.3",
  "com.typesafe" % "config" % "1.0.2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.jsoup" % "jsoup" % "1.7.2",
  "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"
)