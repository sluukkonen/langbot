
package actions

import org.scalatest.{FlatSpec, Matchers}
import response.{ErrorResponse, SuccessResponse}

class ScalaActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new ScalaAction

  private val newline = System.lineSeparator

  "A ScalaAction" should "evaluate Scala" in {
    evaluate("1 + 1") should be(SuccessResponse(s"res0: Int = 2${newline}"))
  }

  it should "should capture standard output" in {
    evaluate("List(1, 2, 3).map(println)") should
      be(SuccessResponse(s"1${newline}2${newline}3${newline}res1: List[Unit] = List((), (), ())${newline}"))
  }

  it should "remember state between invocations" in {
    evaluate("val a = 1")
    evaluate("a") should be(SuccessResponse(s"res2: Int = 1${newline}"))
  }

}
