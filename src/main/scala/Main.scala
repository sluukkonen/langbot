import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._

object Main {
  def main(args: Array[String]) = {
    val config = ConfigFactory.load("application.conf")
    val bot = new LangBot(
      config.getString("nick"),
      config.getString("server"),
      config.getStringList("channels").asScala
    )
    bot.connect()
  }
}
