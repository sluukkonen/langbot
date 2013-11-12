package actions

import clojure.lang._
import scala.concurrent._
import response.{ErrorResponse, SuccessResponse, Response}
import java.io.{PrintWriter, ByteArrayOutputStream}
import scala.concurrent.ExecutionContext.Implicits._

class ClojureAction extends Action with Resettable[Response] {

  val outputStream = new ByteArrayOutputStream
  val writer = new PrintWriter(outputStream)

  val out = RT.`var`("clojure.core", "*out*")
  val ns = RT.`var`("clojure.core", "*ns*")
  val userNs = Namespace.findOrCreate(Symbol.create("user"))

  val bindings = PersistentHashMap.create(out, writer, ns, userNs)

  def evaluate(message: String): Future[Response] = future {
    blocking {
      try {
        Var.pushThreadBindings(bindings)
        val (result, output) = resettingAndFlushing(outputStream, writer) {
          RT.printString(Compiler.eval(RT.readString(message)))
        }
        SuccessResponse(result, output)
      } catch {
        case e: Exception => ErrorResponse(e.getMessage)
      } finally {
        Var.popThreadBindings()
      }
    }
  }
}
