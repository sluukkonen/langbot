package actions

import org.scalatest._
import response.SuccessResponse

class RubyActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new RubyAction

  "A RubyAction" should "evaluate ruby" in {
    val result = evaluate("1 + 1")
    result should be(SuccessResponse("2"))
  }

  it should "redirect standard output to the response" in {
    val result = evaluate("print 'lol'")
    result should be(SuccessResponse("nil", "lol"))
  }

  it should "keep state between invocations" in {
    evaluate("a = 1")
    evaluate("a") should be(SuccessResponse("1"))
  }

}
