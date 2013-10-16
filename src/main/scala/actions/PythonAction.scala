package actions

import response.{Response, Error, Success}
import scala.concurrent._
import org.python.util.PythonInterpreter
import java.io.{PrintStream, ByteArrayOutputStream}
import scala.concurrent.ExecutionContext.Implicits._

class PythonAction extends Action with Resettable[Response] {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val python = new PythonInterpreter()

  python.setOut(printStream)
  python.setErr(printStream)

  def evaluate(message: String): Future[Response] = {
    future {
      blocking {
        try {
          val (result, output) = resetting(outputStream)(python.eval(python.compile(message)))
          Success(result.toString, output)
        } catch {
          case e: Exception => Error(e.toString)
        }
      }
    }
  }
}
