package io.shaka.http

import java.io.InputStream

import scala.collection.mutable.ArrayBuffer

object IO {
  def inputStreamToByteArray(is: InputStream): Array[Byte] = {
    val buffer = new Array[Byte](1024 * 8)
    val output = new ArrayBuffer[Byte]()
    Stream.continually(is.read(buffer)).takeWhile(_ != -1).foreach(output ++= buffer.slice(0, _))
    output.toArray
  }

}
