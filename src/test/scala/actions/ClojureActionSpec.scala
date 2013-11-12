package actions

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import response.{SuccessResponse, Response}
import scala.concurrent.Await
import scala.concurrent.duration._

class ClojureActionSpec extends FlatSpec with ShouldMatchers {
  val clojure = new ClojureAction

  "A ClojureAction" should "evaluate Clojure" in {
    val result = evaluate("(+ 1 1)")
    result should be(SuccessResponse("2"))
  }

  it should "redirect standard output to the response" in {
    val result = evaluate("(println \"lol\")")
    result should be(SuccessResponse("nil", "lol\n"))
  }

  it should "keep state between invocations" in {
    evaluate("(def a 1)")
    evaluate("a") should be(SuccessResponse("1"))
  }

  it should "evaluate stuff in the user namespace" in {
    evaluate("(defn b [] 1)") should be(SuccessResponse("#'user/b"))
  }

  private def evaluate(message: String): Response = Await.result(clojure.evaluate(message), 10.seconds)
}
