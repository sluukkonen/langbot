package actions

import java.io.{Writer, ByteArrayOutputStream}


trait Resettable[T] {

  def resetting[T](byteArrayOutputStream: ByteArrayOutputStream)(fn: => T): (T, String) = synchronized {
    try {
      val result = fn
      (result, byteArrayOutputStream.toString)
    } finally {
      byteArrayOutputStream.reset()
    }
  }

  def resettingAndFlushing[T](byteArrayOutputStream: ByteArrayOutputStream,
                              writer: Writer)(fn: => T): (T, String) = synchronized {
    try {
      val result = fn
      writer.flush()
      (result, byteArrayOutputStream.toString)
    } finally {
      byteArrayOutputStream.reset()
    }
  }


}
