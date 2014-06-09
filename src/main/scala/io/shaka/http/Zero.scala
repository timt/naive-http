package io.shaka.http

object Zero {
  def toOption(value: Array[Byte]): Option[Array[Byte]] = if (value.length == 0) None else Some(value)
}