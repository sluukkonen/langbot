package actions

import org.scalatest.{Matchers, FlatSpec}
import scala.concurrent.Await
import scala.concurrent.duration._
import response.SuccessResponse

class BrainFuckActionSpec extends FlatSpec with Matchers {
  val brainfuck = new BrainFuckAction


  "A BrainFuckAction" should "evaluate Brainfuck" in {
    val result = evaluate("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++" +
      ".------.--------.>+.>.")
    result should be(SuccessResponse("Hello World!\n"))
  }

  def evaluate(message: String) = Await.result(brainfuck.evaluate(message), 2.seconds)
}
