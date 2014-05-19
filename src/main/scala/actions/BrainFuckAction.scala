package actions

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import response.{ErrorResponse, SuccessResponse, Response}
import scala.collection.mutable
import scala.annotation.tailrec

class BrainFuckAction extends Action {

  def evaluate(program: String): Future[Response] = Future {

    val tape = new Array[Int](30000)
    var ip, dp = 0
    val jumpTable = new mutable.HashMap[Int, Int]()
    val output = new StringBuilder

    try {
      while (ip < program.length) {
        program.charAt(ip) match {
          case '>' => dp += 1
          case '<' => dp -= 1
          case '+' => tape(dp) = (tape(dp) + 1) & 0xff
          case '-' => tape(dp) = (tape(dp) - 1) & 0xff
          case '.' => output.append(tape(dp).toChar)
          case '[' => if (tape(dp) == 0) ip = jumpTable.getOrElseUpdate(ip, jumpForward(program, ip))
          case ']' => if (tape(dp) != 0) ip = jumpTable.getOrElseUpdate(ip, jumpBackward(program, ip))
          case _ =>
        }
        ip += 1
      }
      SuccessResponse(output.mkString)
    } catch {
      case e: Exception => ErrorResponse(e.getMessage)
    }
  }

  private def jumpForward(program: String, ip: Int): Int = jumpForward(program, ip + 1, 1)

  private def jumpBackward(program: String, ip: Int): Int = jumpBackward(program, ip - 1, 1)

  @tailrec
  private def jumpForward(program: String, ip: Int, depth: Int): Int = {
    if (depth == 0)
      ip - 1
    else {
      program.charAt(ip) match {
        case '[' => jumpForward(program, ip + 1, depth + 1)
        case ']' => jumpForward(program, ip + 1, depth - 1)
        case _ => jumpForward(program, ip + 1, depth)
      }
    }
  }

  @tailrec
  private def jumpBackward(program: String, ip: Int, depth: Int): Int = {
    if (depth == 0)
      ip + 1
    else {
      program.charAt(ip) match {
        case '[' => jumpBackward(program, ip - 1, depth - 1)
        case ']' => jumpBackward(program, ip - 1, depth + 1)
        case _ => jumpBackward(program, ip - 1, depth)
      }
    }
  }
}
