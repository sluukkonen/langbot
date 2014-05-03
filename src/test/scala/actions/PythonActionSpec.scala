package actions

import org.scalatest.{Matchers, FlatSpec}
import response.SuccessResponse

class PythonActionSpec extends FlatSpec with Matchers with Evaluatable {

  val action = new PythonAction

  "A PythonAction" should "evaluate python" in {
    val result = evaluate("1 + 1")
    result should be(SuccessResponse("2"))
  }

  it should "redirect standard output to the response" in {
    val result = evaluate("for i in [1,2,3]: print i")
    val lineSeparator = System.lineSeparator
    result should be(SuccessResponse("None", s"1${lineSeparator}2${lineSeparator}3${lineSeparator}"))
  }

  it should "keep state between invocations" in {
    evaluate("a = 1")
    evaluate("a") should be(SuccessResponse("1"))
  }

}
