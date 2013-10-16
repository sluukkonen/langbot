package actions

import scala.concurrent._
import org.mozilla.javascript._
import org.mozilla.javascript.tools.shell.Global
import java.io.{PrintStream, ByteArrayOutputStream}
import response.{Error, Success}
import scala.concurrent.ExecutionContext.Implicits._

class JsAction extends Action {

  override def evaluate(message: String) = {
    future {
      blocking {
        val ctx = Context.enter
        val scope = new Global(ctx)

        val outputStream = new ByteArrayOutputStream
        val printStream = new PrintStream(outputStream)

        scope.setOut(printStream)
        scope.setErr(printStream)

        try {
          val result = ctx.evaluateString(scope, message, "unknown", 1, null)
          Success(Context.toString(result), outputStream.toString)
        } catch {
          case e: Exception => Error(e.getMessage)
        } finally {
          Context.exit()
        }
      }
    }
  }
}