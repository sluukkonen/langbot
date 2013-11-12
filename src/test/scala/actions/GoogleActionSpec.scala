package actions

import scala.concurrent._
import scala.concurrent.duration._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import response.{Response, SuccessResponse}


class GoogleActionSpec extends FlatSpec with ShouldMatchers {

  val google = new GoogleAction

  "A GoogleAction" should "search the keyword in google and return the first response" in {
    val response = evaluate("Eficode")
    response should be(SuccessResponse("Eficode | Eficode (http://eficode.fi/)"))
  }

  private def evaluate(message: String): Response = Await.result(google.evaluate(message), 10.seconds)
}
