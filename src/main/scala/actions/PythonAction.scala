package actions

import response.{Response, Error, Success}
import scala.concurrent._
import org.python.util.PythonInterpreter
import java.io.{PrintStream, ByteArrayOutputStream}
import scala.concurrent.ExecutionContext.Implicits._

class PythonAction extends Action {

  def evaluate(message: String): Future[Response] = {
    future {
      blocking {
        val python = new PythonInterpreter()

        val outputStream = new ByteArrayOutputStream
        val printStream = new PrintStream(outputStream)

        python.setOut(printStream)
        python.setErr(printStream)

        try {
          val result = python.eval(python.compile(message))
          Success(result.toString, outputStream.toString)
        } catch {
          case e: Exception => Error(e.toString)
        } finally {
          python.cleanup()
        }
      }
    }
  }
}
