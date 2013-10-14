package actions

import scala.concurrent.Future
import response.Response

trait Action {
  def evaluate(message: String): Future[Response]
}
