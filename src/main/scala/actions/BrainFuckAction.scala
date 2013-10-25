package actions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import response.{Error, Success, Response}

class BrainFuckAction extends Action {

  def evaluate(program: String): Future[Response] = future {

    val buffer = new Array[Int](30000)
    var ip = 0
    var dp = 0
    val output = new StringBuilder

    try {
      while (ip < program.length) {
        program.charAt(ip) match {
          case '>'                    => dp += 1
          case '<'                    => dp -= 1
          case '+'                    => buffer(dp) += 1
          case '-'                    => buffer(dp) -= 1
          case '.'                    => output.append(buffer(dp).asInstanceOf[Char])
          case '[' if buffer(dp) == 0 => ip = jumpForward(program, ip + 1, 1)
          case ']' if buffer(dp) != 0 => ip = jumpBackward(program, ip - 1, 1)
          case _                      =>
        }
        ip += 1
      }
    } catch {
      case e: Exception => Error(e.getMessage)
    }

    Success(output.toString)
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
