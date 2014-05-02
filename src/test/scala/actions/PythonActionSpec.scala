package actions

import org.scalatest.FlatSpec
import org.scalatest.matchers.{BePropertyMatcher, ShouldMatchers}
import response.{Response, ErrorResponse, SuccessResponse}
import scala.concurrent.Await
import scala.concurrent.duration._

class PythonActionSpec extends FlatSpec with ShouldMatchers {

  val python = new PythonAction

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

  private def evaluate(message: String): Response = Await.result(python.evaluate(message), 10.seconds)
}
