package actions

import org.scalatest.{Matchers, FlatSpec}
import response.SuccessResponse

class BrainFuckActionSpec extends FlatSpec with Matchers with Evaluatable {

  val action = new BrainFuckAction

  "A BrainFuckAction" should "evaluate Brainfuck" in {
    val result = evaluate("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++" +
      ".------.--------.>+.>.")
    result should be(SuccessResponse("Hello World!\n"))
  }
}
