package actions

import response.{Response, ErrorResponse, SuccessResponse}
import scala.concurrent._
import org.python.util.PythonInterpreter
import java.io.{PrintStream, ByteArrayOutputStream}
import scala.concurrent.ExecutionContext.Implicits._

class PythonAction extends Action with Resettable {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val python = new PythonInterpreter()

  python.setOut(printStream)
  python.setErr(printStream)

  def evaluate(message: String): Future[Response] = {
    Future {
      blocking {
        try {
          val (result, output) = resetting(outputStream)(python.eval(python.compile(message)))
          SuccessResponse(result.toString, output)
        } catch {
          case e: Exception => ErrorResponse(e.toString)
        }
      }
    }
  }
}
