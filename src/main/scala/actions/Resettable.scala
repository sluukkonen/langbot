package actions

import java.io.ByteArrayOutputStream


trait Resettable[T] {

  val systemOut = System.out
  val systemErr = System.err

  def resetting[T](byteArrayOutputStream: ByteArrayOutputStream)(fn: => T): (T, String) = synchronized {
    try {
      val result = fn
      (result, byteArrayOutputStream.toString)
    } finally {
      byteArrayOutputStream.reset
    }
  }
}
