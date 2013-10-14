package actions

import response.{Success, Response}
import dispatch._, Defaults._
import org.jsoup.Jsoup

class LinkAction extends Action {


  def evaluate(message: String) = {
    val request = url(message).addHeader(
      "User-Agent",
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:22.0) Gecko/20100101 Firefox/22.0"
    )
    val response = Http(request OK as.String)
    response map parseTitle
  }

  def parseTitle(html: String) = {
    val doc = Jsoup.parse(html)
    val title = Option(doc.select("title").first)
    title map (t => Success(t.text.trim)) getOrElse(Success(""))
  }

}
