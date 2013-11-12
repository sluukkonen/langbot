package response

sealed trait Response

case class SuccessResponse(result: String, output: String = "") extends Response
case class ErrorResponse(message: String) extends Response
object NoResponse extends Response
