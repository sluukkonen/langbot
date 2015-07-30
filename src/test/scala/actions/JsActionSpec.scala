package actions

import org.scalatest._
import response.{ErrorResponse, SuccessResponse}

class JsActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new JsAction

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

  it should "return error messages" in {
    evaluate("foo") should be(ErrorResponse("ReferenceError: \"foo\" is not defined in <eval> at line number 1"))
  }

}
