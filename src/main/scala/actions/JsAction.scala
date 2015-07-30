package actions

import java.io.{ByteArrayOutputStream, PrintWriter}
import javax.script.ScriptEngineManager

import response.Response

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class JsAction extends Action with Resettable {

  private val engine = new ScriptEngineManager(null).getEngineByName("nashorn")

  private val outputStream = new ByteArrayOutputStream
  private val writer = new PrintWriter(outputStream)

  engine.getContext.setWriter(writer)
  engine.getContext.setErrorWriter(writer)

  override def evaluate(message: String) = {
    Future {
      blocking {
        Response.create {
          val (result, output) = resettingAndFlushing(outputStream, writer)(engine.eval(message))
          (toStringOrUndefined(result), output)
        }
      }
    }
  }

  private def toStringOrUndefined(result: AnyRef): String = Option(result).fold("undefined")(_.toString)
}