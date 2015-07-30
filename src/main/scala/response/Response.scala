package response

import scala.util.{Failure, Success, Try}

sealed trait Response

case class SuccessResponse(result: String, output: String = "") extends Response
case class ErrorResponse(message: String) extends Response
object NoResponse extends Response

object Response {
  def create(block: => (String, String)): Response = {
    Try(block) match {
      case Success((response, output)) => SuccessResponse(response, output)
      case Failure(e) => ErrorResponse(Option(e.getMessage).getOrElse(e.toString))
    }
  }
}
