assemblyMergeStrategy in assembly := {
  case PathList("com", "kenai", "jffi", xs@_*) => MergeStrategy.first
  case PathList("jnr", "netdb", xs@_*) => MergeStrategy.first
  case PathList("org", "joda", "time", xs@_*) => MergeStrategy.first
  case "pom.properties" => MergeStrategy.first
  case x => (assemblyMergeStrategy in assembly).value(x)
}

test in assembly := {}
