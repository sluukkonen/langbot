package actions

import java.io.{ByteArrayOutputStream, PrintStream}

import org.jruby.runtime.scope.ManyVarsDynamicScope
import org.jruby.{Ruby, RubyInstanceConfig}
import response.Response

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class RubyAction extends Action with Resettable {

  val outputStream = new ByteArrayOutputStream
  val printStream = new PrintStream(outputStream)

  val config = new RubyInstanceConfig

  config.setOutput(printStream)
  config.setError(printStream)

  val ruby = Ruby.newInstance(config)

  val scope = new ManyVarsDynamicScope(ruby.getStaticScopeFactory.newEvalScope(
    ruby.getCurrentContext.getCurrentScope.getStaticScope), ruby.getCurrentContext.getCurrentScope)

  override def evaluate(message: String) = {
    Future {
      blocking {
        Response.create {
          val (result, output) = resetting(outputStream)(ruby.evalScriptlet(message, scope))
          (result.inspect.toString, output)
        }
      }
    }
  }
}