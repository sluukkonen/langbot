package actions

import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest.matchers.ShouldMatchers
import response.{Response, Error, Success}

class JsActionSpec extends FlatSpec with ShouldMatchers {

  val js = new JsAction

  "A JsAction" should "evaluate JavaScript" in {
    val result = evaluate("1 + 1")
    result should be(Success("2"))
  }

  it should "should redirect standard output to the response" in {
    val result = evaluate("print('lol')")
    result should be(Success("undefined", "lol\n"))
  }

  it should "not keep state between invocations" in {
    evaluate("a = 1")
    val result = evaluate("a")
    result should be(Error("ReferenceError: \"a\" is not defined. (unknown#1)"))
  }

  private def evaluate(message: String): Response = Await.result(js.evaluate(message), 10.seconds)
}
