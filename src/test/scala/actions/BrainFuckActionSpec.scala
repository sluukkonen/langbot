package actions

import org.scalatest.{Matchers, FlatSpec}
import response.{ErrorResponse, SuccessResponse}
import scala.io.Source

class BrainFuckActionSpec extends FlatSpec with Matchers with Evaluatable {

  override val action = new BrainFuckAction

  "A BrainFuckAction" should "evaluate Brainfuck" in {
    evaluate(loadProgram("hello")) should be(SuccessResponse("Hello World!\n"))
  }

  it should "be reasonably fast" in {
    evaluate(loadProgram("benchmark")) should be(SuccessResponse("OK"))
  }

  private def loadProgram(name: String): String =
    Source.fromURL(getClass.getResource(s"/brainfuck/${name}.bf")).getLines.mkString
}
