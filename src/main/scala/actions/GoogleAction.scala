package actions

import dispatch._, Defaults._
import org.jsoup.Jsoup
import response.Success

class GoogleAction extends Action {

  val baseRequest = url("https://www.google.fi/search").addHeader(
    "User-Agent",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:22.0) Gecko/20100101 Firefox/22.0"
  )

  override def evaluate(message: String) = {
    val request = baseRequest <<? Map("query" -> message)
    val response = Http(request OK as.String)
    response map parseResult
  }

  private def parseResult(response: String) = {
    val doc = Jsoup.parse(response.asInstanceOf[java.lang.String])
    val firstResult = doc.select("#ires ol li h3 a").first
    val text = firstResult.text
    val url = firstResult.attr("href")
    Success(s"$text ($url)")
  }

}
