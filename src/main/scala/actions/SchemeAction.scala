package actions

import jscheme.{REPL, JScheme}
import response.Response

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Future, _}

class SchemeAction extends Action with Resettable {

  val scheme = new JScheme()

  override def evaluate(message: String): Future[Response] = Future {
    blocking {
      Response.create {
        val result = REPL.printToString(scheme.eval(message), false)
        (result, "")
      }
    }
  }
}
