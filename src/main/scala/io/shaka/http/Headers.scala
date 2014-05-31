package io.shaka.http

import io.shaka.http.Http.Header
import io.shaka.http.HttpHeader._

case class Headers(headers: List[Header]) {
  def contains(header: HttpHeader): Boolean = headers.exists(_._1 == header)

  def apply(header: HttpHeader): List[String] = headers.filter(_._1 == header).map(_._2)

  def foreach[A](f: Header => A): Unit = headers.foreach(f)

  def ::(header: Header): Headers = Headers(headers.::(header))

  def map[A](f: Header => A) = headers.map(f)

  def find(p: Header => Boolean): Option[Header] = headers.find(p)

  def filter(p: Header => Boolean): List[Header] = headers.filter(p)
}

object Headers {
  val Empty = Headers(List())
  import scala.collection.JavaConversions.mapAsScalaMap
  import scala.collection.JavaConversions.collectionAsScalaIterable
  def toHeaders(rawHeaders: java.util.Map[String, java.util.List[String]]):Headers = Headers(rawHeaders.toList.flatMap(pair => pair._2.map((httpHeader(pair._1), _))))
}
