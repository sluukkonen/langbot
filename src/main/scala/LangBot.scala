import org.jibble.pircbot.PircBot
import actions._
import response.{NoResponse, Error, Success, Response}
import scala.concurrent.ExecutionContext.Implicits.global

class LangBot(nickName: String, server: String, channels: Seq[String]) extends PircBot {

  def makePattern(prefix: String) = ("""^\.""" + prefix + """\s(.*)""").r

  var ruby = new RubyAction
  var js = new JsAction
  var python = new PythonAction
  val link = new LinkAction
  val google = new GoogleAction
  val clojure = new ClojureAction

  val rubyPattern = makePattern("rb")
  val jsPattern = makePattern("js")
  val pythonPattern = makePattern("py")
  val clojurePattern = makePattern("clj")
  val httpPattern = """^http://.*""".r
  val httpsPattern = """^https://.*""".r
  val googlePattern = makePattern("google")
  val resetPattern = """^\.reset.*""".r

  setName(nickName)
  setVerbose(true)
  setEncoding("UTF-8")

  def connect(): Unit = {
    connect(server)
    channels foreach joinChannel
  }

  override def onMessage(channel: String, sender: String, login: String, host: String,
                         message: String) = message match {
    case rubyPattern(msg)    => evaluate(ruby, msg, channel)
    case jsPattern(msg)      => evaluate(js, msg, channel)
    case pythonPattern(msg)  => evaluate(python, msg, channel)
    case clojurePattern(msg) => evaluate(clojure, msg, channel)
    case googlePattern(msg)  => evaluate(google, msg, channel)
    case httpsPattern()      => evaluate(link, message, channel)
    case httpPattern()       => evaluate(link, message, channel)
    case resetPattern()      => reset; sendMessage(channel, "=> Ready!")
    case _                   => println("No match")
  }

  private def evaluate(action: Action, message: String, channel: String) =
    action.evaluate(message) foreach (sendResponse(channel, _))

  private def sendResponse(channel: String, response: Response) = response match {
    case Success(result, output) =>
      sendFilteredResponse(channel, output)
      sendFilteredResponse(channel, s"=> $result")
    case Error(message)          =>
      sendFilteredResponse(channel, message)
    case NoResponse              => // Pass
  }

  private def sendFilteredResponse(channel: String, response: String) =
    filterResponse(response) foreach (sendMessage(channel, _))

  private def filterResponse(response: String) = response.split("\n") filterNot (_.isEmpty) take 5

  private def reset = {
    ruby = new RubyAction
    js = new JsAction
    python = new PythonAction
  }

}
