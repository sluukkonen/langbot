package actions

import java.io.{ByteArrayOutputStream, PrintStream}

import org.python.util.PythonInterpreter
import response.Response

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class PythonAction extends Action with Resettable {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val python = new PythonInterpreter()

  python.setOut(printStream)
  python.setErr(printStream)

  def evaluate(message: String): Future[Response] = {
    Future {
      blocking {
        Response.create {
          val (result, output) = resetting(outputStream)(python.eval(python.compile(message)))
          (result.toString, output)
        }
      }
    }
  }
}
