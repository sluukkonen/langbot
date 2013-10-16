package actions

import scala.concurrent._
import org.mozilla.javascript._
import org.mozilla.javascript.tools.shell.Global
import java.io.{PrintStream, ByteArrayOutputStream}
import response.{Response, Error, Success}
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
          Success(Context.toString(result), output)
        } catch {
          case e: Exception => Error(e.getMessage)
        } finally {
          Context.exit()
        }
      }
    }
  }
}