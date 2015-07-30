package actions

import java.io.{ByteArrayOutputStream, PrintStream}

import response.Response

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._
import scala.tools.nsc.interpreter.IMain

class ScalaAction extends Action with Resettable {

  private val scala = new IMain()
  scala.settings.usejavacp.value = true

  private val outputStream = new ByteArrayOutputStream
  private val consoleOut = new PrintStream(outputStream)

  override def evaluate(message: String): Future[Response] = Future {
    blocking {
      Response.create {
        // The interpreter doesn't return a useful return value, it just prints stuff to Console.out
        val (_, output) = resetting(outputStream)(Console.withOut(consoleOut)(scala.interpret(message)))
        (output, "")
      }
    }
  }

}
