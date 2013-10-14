package actions

import org.scalatest.FlatSpec
import org.scalatest.matchers.{BePropertyMatcher, ShouldMatchers}
import response.{Response, Error, Success}
import scala.concurrent.Await
import scala.concurrent.duration._

class PythonActionSpec extends FlatSpec with ShouldMatchers {

  val python = new PythonAction

  "A PythonAction" should "evaluate python" in {
    val result = evaluate("1 + 1")
    result should be(Success("2"))
  }

  it should "redirect standard output to the response" in {
    val result = evaluate("for i in [1,2,3]: print i")
    result should be(Success("None", "1\n2\n3\n"))
  }

  it should "not keep state between invocations" in {
    evaluate("a = 1")
    val result = evaluate("a")
    result match {
      case Error(message) => message should startWith("Traceback (most recent call last):")
      case _ => fail("result should be an Error object")
    }
  }

  private def evaluate(message: String): Response = Await.result(python.evaluate(message), 10.seconds)
}
