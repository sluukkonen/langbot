package actions

import response.{NoResponse, SuccessResponse, Response}
import dispatch._, Defaults._
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class LinkAction extends Action {


  def evaluate(message: String) = {
    val request = url(message).addHeader(
      "User-Agent",
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:22.0) Gecko/20100101 Firefox/22.0"
    )
    val response = Http.configure(_ setFollowRedirects true)(request OK as.String)
    response map parseTitle
  }

  private def parseTitle(html: String): Response = {
    val doc = Jsoup parse html
    val title = Option(doc.select("title").first)
    title map text getOrElse NoResponse
  }

  private def text(e: Element) = SuccessResponse(e.text.trim)

}
