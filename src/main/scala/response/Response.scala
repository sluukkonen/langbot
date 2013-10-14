package response

sealed trait Response

case class Success(result: String, output: String = "") extends Response
case class Error(message: String) extends Response
