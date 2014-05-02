package actions

import scala.concurrent._
import java.io.{PrintWriter, ByteArrayOutputStream}
import response.{ErrorResponse, SuccessResponse}
import scala.concurrent.ExecutionContext.Implicits._
import javax.script.ScriptEngineManager

class JsAction extends Action with Resettable {

  private val engine = new ScriptEngineManager(null).getEngineByName("nashorn")

  private val outputStream = new ByteArrayOutputStream
  private val writer = new PrintWriter(outputStream)

  engine.getContext.setWriter(writer)
  engine.getContext.setErrorWriter(writer)

  override def evaluate(message: String) = {
    Future {
      blocking {
        try {
          val (result, output) = resettingAndFlushing(outputStream, writer)(engine.eval(message))
          SuccessResponse(toStringOrUndefined(result), output)
        } catch {
          case e: Exception => ErrorResponse(e.getMessage)
        }
      }
    }
  }

  private def toStringOrUndefined(result: AnyRef): String = Option(result).fold("undefined")(_.toString)
}