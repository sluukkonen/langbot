package actions

import org.scalatest.{Matchers, FlatSpec, FunSuite}
import response.{ErrorResponse, SuccessResponse}

class SchemeActionTest extends FlatSpec with Matchers with Evaluatable {

  val action = new SchemeAction

  "A SchemeAction" should "evaluate scheme" in {
    evaluate("(+ 1 1)") should be(SuccessResponse("2"))
  }

  it should "keep state between invocations" in {
    evaluate("(define (square x) (* x x))")
    evaluate("(square 2)") should be(SuccessResponse("4"))
  }

  it should "return error messages" in {
    evaluate("asdf") should be(ErrorResponse("ERROR: undefined variable \"asdf\""))
  }

}
