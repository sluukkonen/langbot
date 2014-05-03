package actions

import org.scalatest.{Matchers, FlatSpec}
import response.SuccessResponse

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

}
