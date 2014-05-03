package actions

import scala.concurrent.Await
import scala.concurrent.duration._

trait Evaluatable {
  val action: Action
  val timeout = 5.seconds

  protected def evaluate(message: String) = Await.result(action.evaluate(message), timeout)
}
