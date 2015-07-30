package actions

import org.scalatest.{FlatSpec, Matchers}
import response.{ErrorResponse, SuccessResponse}

class ClojureActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new ClojureAction

  "A ClojureAction" should "evaluate Clojure" in {
    val result = evaluate("(+ 1 1)")
    result should be(SuccessResponse("2"))
  }

  it should "redirect standard output to the response" in {
    val result = evaluate("(println \"lol\")")
    result should be(SuccessResponse("nil", s"lol${System.lineSeparator}"))
  }

  it should "keep state between invocations" in {
    evaluate("(def a 1)")
    evaluate("a") should be(SuccessResponse("1"))
  }

  it should "evaluate stuff in the user namespace" in {
    evaluate("(defn b [] 1)") should be(SuccessResponse("#'user/b"))
  }

  it should "use Clojure 1.7.0" in {
    evaluate("(clojure-version)") should be(SuccessResponse("\"1.7.0\""))
  }

  it should "return error messages" in {
    evaluate("foo") should be(ErrorResponse("java.lang.RuntimeException: Unable to resolve symbol: foo in this context, compiling:(null:0:0)"))
  }

}
