package actions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits._
import scala.tools.nsc.interpreter.IMain
import response.{ErrorResponse, SuccessResponse, Response}
import javax.script.ScriptEngineManager
import java.io.{PrintStream, ByteArrayOutputStream}

class ScalaAction extends Action with Resettable {

  private val scala = new IMain()
  scala.settings.usejavacp.value = true

  private val outputStream = new ByteArrayOutputStream
  private val consoleOut = new PrintStream(outputStream)

  override def evaluate(message: String): Future[Response] = Future {
    blocking {
      try {
        // The interpreter doesn't return a useful return value, it just prints stuff to Console.out
        val (_, output) = resetting(outputStream)(Console.withOut(consoleOut)(scala.interpret(message)))
        SuccessResponse(output)
      } catch {
        case e: Exception => ErrorResponse(e.getMessage)
      }
    }
  }
  private def toStringOrUnit(result: AnyRef): String = Option(result).fold("null")(_.toString)
}
