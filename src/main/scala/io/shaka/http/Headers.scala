package io.shaka.http

import io.shaka.http.Http.Header

case class Headers(headers: List[Header]) {
  def contains(header: HttpHeader): Boolean = headers.exists(_._1 == header)

  def apply(header: HttpHeader): List[String] = headers.filter(_._1 == header).map(_._2)

  def foreach[A](f: Header => A): Unit = headers.foreach(f)

  def ::(header: Header): Headers = Headers(headers.::(header))
}

object Headers {
  val Empty = Headers(List())
}
