package actions

import org.jruby.{Ruby, RubyInstanceConfig}
import scala.concurrent._
import java.io.{PrintStream, ByteArrayOutputStream}
import response.{Error, Success}
import scala.concurrent.ExecutionContext.Implicits._

class RubyAction extends Action {

  override def evaluate(message: String) = {
    future {
      blocking {
        val config = new RubyInstanceConfig

        val outputStream = new ByteArrayOutputStream
        val printStream = new PrintStream(outputStream)

        config.setOutput(printStream)
        config.setError(printStream)

        val ruby = Ruby.newInstance(config)

        try {
          val result = ruby.evalScriptlet(message)
          Success(result.inspect.toString, outputStream.toString)
        } catch {
          case e: Exception => Error(e.getMessage)
        }

      }
    }
  }
}