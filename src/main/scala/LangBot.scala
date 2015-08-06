import actions._
import akka.actor.ActorSystem
import org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4
import response.{ErrorResponse, NoResponse, Response, SuccessResponse}
import slack.rtm.SlackRtmClient

class LangBot(slackApiKey: String) {

  implicit val system = ActorSystem("slack")
  implicit val ec = system.dispatcher

  val client = new SlackRtmClient(slackApiKey)

  def makePattern(prefix: String) = ("""^\.""" + prefix + """\s(.*)""").r

  var ruby = new RubyAction
  var js = new JsAction
  var python = new PythonAction
  var clojure = new ClojureAction
  var scala = new ScalaAction
  var scheme = new SchemeAction
  val brainfuck = new BrainFuckAction

  val rubyPattern = makePattern("rb")
  val jsPattern = makePattern("js")
  val pythonPattern = makePattern("py")
  val clojurePattern = makePattern("clj")
  val brainfuckPattern = makePattern("bf")
  val schemePattern = makePattern("scm")
  val scalaPattern = makePattern("scala")
  val resetPattern = """^\.reset.*""".r

  client onMessage { message =>
    unescapeHtml4(message.text) match {
      case rubyPattern(msg) => evaluate(ruby, msg, message.channel)
      case jsPattern(msg) => evaluate(js, msg, message.channel)
      case pythonPattern(msg) => evaluate(python, msg, message.channel)
      case clojurePattern(msg) => evaluate(clojure, msg, message.channel)
      case brainfuckPattern(msg) => evaluate(brainfuck, msg, message.channel)
      case scalaPattern(msg) => evaluate(scala, msg, message.channel)
      case schemePattern(msg) => evaluate(scheme, msg, message.channel)
      case resetPattern() => reset(); sendMessage(message.channel, "=> Ready!")
      case _ => // Pass
    }
  }

  private def sendMessage(channel: String, message: String) = {
    client.sendMessage(channel, message)
  }

  private def evaluate(action: Action, message: String, channel: String) =
    action.evaluate(message) foreach (sendResponse(channel, _))

  private def sendResponse(channel: String, response: Response) = response match {
    case SuccessResponse(result, output) =>
      sendFilteredResponse(channel, output)
      sendFilteredResponse(channel, s"⇒ $result")
    case ErrorResponse(message) =>
      sendFilteredResponse(channel, message)
    case NoResponse => // Pass
  }

  private def sendFilteredResponse(channel: String, response: String) =
    filterResponse(response) foreach (sendMessage(channel, _))

  private def filterResponse(response: String) = response.split("\n") filterNot (_.isEmpty) take 5

  private def reset(): Unit = {
    ruby = new RubyAction
    js = new JsAction
    python = new PythonAction
    clojure = new ClojureAction
    scala = new ScalaAction
    scheme = new SchemeAction
  }

}
