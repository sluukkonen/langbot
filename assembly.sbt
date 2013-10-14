import AssemblyKeys._

assemblySettings

mergeStrategy in assembly <<= (mergeStrategy in assembly) { old =>
  {
    case PathList("com", "kenai", "jffi", xs @ _*) => MergeStrategy.first
    case PathList("jnr", "netdb", xs @ _*) => MergeStrategy.first
    case x => old(x)
  }
}
