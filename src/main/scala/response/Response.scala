package response

import scala.util.{Failure, Success, Try}

sealed trait Response

case class SuccessResponse(result: String, output: String = "") extends Response
case class ErrorResponse(message: String) extends Response
object NoResponse extends Response

object Response {
  def create(block: (String, String)) = {
    Try(block) match {
      case Success((response, output)) => SuccessResponse.tupled(response, output)
      case Failure(e) => ErrorResponse(e.getMessage)
    }
  }
}
