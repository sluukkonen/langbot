name := "langbot"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "org.jruby" % "jruby-complete" % "9.0.0.0",
  "org.python" % "jython" % "2.5.3",
  "org.clojure" % "clojure" % "1.7.0",
  "com.typesafe" % "config" % "1.3.0",
  "org.scala-lang" % "scala-compiler" % "2.11.7",
  "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test",
  "log4j" % "log4j" % "1.2.17",
  "com.github.gilbertw1" %% "slack-scala-client" % "0.1.1",
  "org.apache.commons" % "commons-lang3" % "3.4"
)

mainClass in run := Some("Main")
