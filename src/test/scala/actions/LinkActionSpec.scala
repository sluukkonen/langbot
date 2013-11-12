package actions

import scala.concurrent._
import scala.concurrent.duration._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import response.{NoResponse, Response, SuccessResponse}


class LinkActionSpec extends FlatSpec with ShouldMatchers {

  val link = new LinkAction

  "A LinkAction" should "parse a link and return the title (Youtube)" in {
    evaluate("https://www.youtube.com/watch?v=2Z4m4lnjxkY") should
      be(SuccessResponse("Trololo Sing Along! - YouTube"))
  }

  it should "parse a link and return the title (Eficode)" in {
    evaluate("http://eficode.fi/") should be(SuccessResponse("Eficode | Eficode"))
  }

  it should "parse a link and return the title (Google)" in {
    evaluate("https://www.google.fi/search?q=Hacker%20news") should be(SuccessResponse("Hacker news - Google-haku"))
  }

  it should "return nothing if the content isn't HTML" in {
    evaluate("http://i.imgur.com/sXVYDuCh.jpg") should be(NoResponse)
  }

  private def evaluate(message: String): Response = Await.result(link.evaluate(message), 10.seconds)
}
