package actions

import org.scalatest._
import scala.concurrent.Await
import scala.concurrent.duration._
import org.scalatest.matchers.ShouldMatchers
import response.{Response, ErrorResponse, SuccessResponse}

class JsActionSpec extends FlatSpec with ShouldMatchers {

  val js = new JsAction

  "A JsAction" should "evaluate JavaScript" in {
    val result = evaluate("1 + 1")
    result should be(SuccessResponse("2"))
  }

  it should "should redirect standard output to the response" in {
    val result = evaluate("print('lol')")
    result should be(SuccessResponse("undefined", s"lol${System.lineSeparator}"))
  }

  it should "keep state between invocations" in {
    evaluate("a = 1")
    evaluate("a") should be(SuccessResponse("1"))
  }

  private def evaluate(message: String): Response = Await.result(js.evaluate(message), 10.seconds)
}
