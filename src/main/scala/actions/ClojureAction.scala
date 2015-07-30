package actions

import java.io.{ByteArrayOutputStream, PrintWriter}

import clojure.java.api.Clojure
import clojure.lang._
import response.{ErrorResponse, Response, SuccessResponse}

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent._

class ClojureAction extends Action with Resettable {

  val outputStream = new ByteArrayOutputStream
  val writer = new PrintWriter(outputStream)

  val out = Clojure.`var`("clojure.core", "*out*")
  val ns = Clojure.`var`("clojure.core", "*ns*")
  val loadString = Clojure.`var`("clojure.core", "load-string")
  val userNs = Namespace.findOrCreate(Symbol.create("user"))

  val bindings = PersistentHashMap.create(out, writer, ns, userNs)

  def evaluate(message: String): Future[Response] = Future {
    blocking {
      try {
        Var.pushThreadBindings(bindings)
        val (result, output) = resettingAndFlushing(outputStream, writer) {
          RT.printString(loadString.invoke(message))
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
