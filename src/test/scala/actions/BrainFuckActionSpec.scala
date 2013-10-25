package actions

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import scala.concurrent.Await
import scala.concurrent.duration._
import response.Success

class BrainFuckActionSpec extends FlatSpec with ShouldMatchers {
  val brainfuck = new BrainFuckAction


  "A BrainFuckAction" should "evaluate Brainfuck" in {
    val result = evaluate("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++" +
      ".------.--------.>+.>.")
    result should be(Success("Hello World!\n"))
  }

  def evaluate(message: String) = Await.result(brainfuck.evaluate(message), 2.seconds)
}
