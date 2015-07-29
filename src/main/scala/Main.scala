import com.typesafe.config.ConfigFactory

object Main {
  def main(args: Array[String]) = {
    val config = ConfigFactory.load("application.conf")
    val bot = new LangBot(config.getString("slackApiKey")
    )
  }
}
