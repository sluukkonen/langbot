package actions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import response.{ErrorResponse, SuccessResponse, Response}

class BrainFuckAction extends Action {

  def evaluate(program: String): Future[Response] = future {

    val tape = new Array[Int](30000)
    var ip = 0
    var dp = 0
    val output = new StringBuilder

    try {
      while (ip < program.length) {
        program.charAt(ip) match {
          case '>'                    => dp += 1
          case '<'                    => dp -= 1
          case '+'                    => tape(dp) += 1
          case '-'                    => tape(dp) -= 1
          case '.'                    => output.append(tape(dp).asInstanceOf[Char])
          case '[' if tape(dp) == 0 => ip = jumpForward(program, ip + 1, 1)
          case ']' if tape(dp) != 0 => ip = jumpBackward(program, ip - 1, 1)
          case _                      =>
        }
        ip += 1
      }
    } catch {
      case e: Exception => ErrorResponse(e.getMessage)
    }

    SuccessResponse(output.toString)
  }

  def jumpForward(program: String, ip: Int, depth: Int): Int = {
    if (depth == 0)
      ip - 1
    else {
      program.charAt(ip) match {
        case '[' => jumpForward(program, ip + 1, depth + 1)
        case ']' => jumpForward(program, ip + 1, depth - 1)
        case _   => jumpForward(program, ip + 1, depth)
      }
    }
  }

  def jumpBackward(program: String, ip: Int, depth: Int): Int = {
    if (depth == 0)
      ip + 1
    else {
      program.charAt(ip) match {
        case '[' => jumpBackward(program, ip - 1, depth - 1)
        case ']' => jumpBackward(program, ip - 1, depth + 1)
        case _   => jumpBackward(program, ip - 1, depth)
      }
    }
  }
}
