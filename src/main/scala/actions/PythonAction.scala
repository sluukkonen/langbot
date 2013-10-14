package actions

import response.{Response, Error, Success}
import scala.concurrent._
import java.util.concurrent.Executors
import org.python.util.PythonInterpreter
import java.io.{PrintStream, ByteArrayOutputStream}

class PythonAction extends Action {

  implicit val executionContext = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))

  def evaluate(message: String): Future[Response] = {
    future {
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
