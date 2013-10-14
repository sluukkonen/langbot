package actions

import scala.concurrent._
import scala.concurrent.duration._
import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import response.{Response, Success}


class LinkActionSpec extends FlatSpec with ShouldMatchers {

  val link = new LinkAction

  "A LinkAction" should "parse a link and return the title (Youtube)" in {
    evaluate("https://www.youtube.com/watch?v=2Z4m4lnjxkY") should
      be(Success("Trololo Sing Along! - YouTube"))
  }

  "A LinkAction" should "parse a link and return the title (Eficode)" in {
    evaluate("http://eficode.fi/") should be(Success("Eficode | Eficode"))
  }

  "A LinkAction" should "parse a link and return the title (Google)" in {
    evaluate("https://www.google.fi/search?q=Hacker%20news") should be(Success("Hacker news - Google-haku"))
  }

  private def evaluate(message: String): Response = Await.result(link.evaluate(message), 10.seconds)
}
