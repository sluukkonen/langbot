# Langbot [![Build Status](https://travis-ci.org/sluukkonen/langbot.png?branch=master)](https://travis-ci.org/sluukkonen/langbot)

A small Slack bot for running code in various programming languages. Written in Scala.

## Commands

* `.rb ...`  run Ruby code (via JRuby)
* `.py ...`  run Python code (via Jython)
* `.js ...`  run JavaScript code (via Rhino)
* `.clj ...` run Clojure code
* `.bf ...`  run Brainfuck code
* `.reset`   reset the language interpreters

## Running it

Edit the configuration file at [src/main/resources/application.conf](src/main/resources/application.conf).

```
$ sbt assembly
$ java -Djava.security.manager -Djava.security.policy=langbot.policy -jar target/scala-2.11/langbot-assembly-1.0.jar
```
