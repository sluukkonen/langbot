package actions

import org.jruby.{Ruby, RubyInstanceConfig}
import scala.concurrent._
import java.io.{PrintStream, ByteArrayOutputStream}
import response.{Response, Error, Success}
import scala.concurrent.ExecutionContext.Implicits._
import org.jruby.runtime.scope.ManyVarsDynamicScope

class RubyAction extends Action with Resettable[Response] {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val config = new RubyInstanceConfig

  config.setOutput(printStream)
  config.setError(printStream)

  val ruby = Ruby.newInstance(config)

  val scope = new ManyVarsDynamicScope(ruby.getStaticScopeFactory.newEvalScope(
    ruby.getCurrentContext.getCurrentScope.getStaticScope), ruby.getCurrentContext.getCurrentScope)

  override def evaluate(message: String) = {
    future {
      blocking {
        try {
          val (result, output) = resetting(outputStream)(ruby.evalScriptlet(message, scope))
          Success(result.inspect.toString, output)
        } catch {
          case e: Exception => Error(e.getMessage)
        }

      }
    }
  }
}