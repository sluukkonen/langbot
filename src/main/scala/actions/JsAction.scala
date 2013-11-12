package actions

import scala.concurrent._
import org.mozilla.javascript._
import org.mozilla.javascript.tools.shell.Global
import java.io.{PrintStream, ByteArrayOutputStream}
import response.{Response, ErrorResponse, SuccessResponse}
import scala.concurrent.ExecutionContext.Implicits._

class JsAction extends Action with Resettable[Response] {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val ctx = Context.enter
  val scope = new Global(ctx)

  scope.setOut(printStream)
  scope.setErr(printStream)


  override def evaluate(message: String) = {
    future {
      blocking {
        Context.enter()
        try {
          val (result, output) = resetting(outputStream)(ctx.evaluateString(scope, message, "unknown", 1, null))
          SuccessResponse(Context.toString(result), output)
        } catch {
          case e: Exception => ErrorResponse(e.getMessage)
        } finally {
          Context.exit()
        }
      }
    }
  }
}