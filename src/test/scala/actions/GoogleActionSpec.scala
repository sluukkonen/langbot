package actions

import org.scalatest.{Matchers, FlatSpec}
import response.SuccessResponse


class GoogleActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new GoogleAction

  "A GoogleAction" should "search the keyword in google and return the first response" in {
    val response = evaluate("Eficode")
    response should be(SuccessResponse("Eficode | Eficode (http://eficode.fi/)"))
  }

}
