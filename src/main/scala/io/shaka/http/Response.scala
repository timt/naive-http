package io.shaka.http

import io.shaka.http.Status.{NOT_FOUND, SEE_OTHER, OK}
import io.shaka.http.HttpHeader.{LOCATION, CONTENT_TYPE}

object Response {
  def respond(content: String): Response = Response().entity(content)
  def respond(content: Array[Byte]): Response = Response().entity(content)
  def seeOther(url: String): Response = respond("").status(SEE_OTHER).header(LOCATION, url)
  val ok = Response().status(OK)
  val notFound = Response().status(NOT_FOUND)
}

case class Response(status: Status = OK, headers: Headers = Headers.Empty, entity: Option[Entity] = None) {
  def status(newStatus: Status) = copy(status = newStatus)
  def header(header: HttpHeader, value: String) = copy(headers = (header, value) :: headers)
  def header(header: HttpHeader) = headers.find(_._1 == header).map(_._2)
  def contentType(value: String) = header(CONTENT_TYPE, value)
  def contentType(value: ContentType) = header(CONTENT_TYPE, value.value)
  def contentType = header(CONTENT_TYPE).map(ContentType.contentType)
  def entity(content: String): Response = copy(entity = Some(Entity(content)))
  def entity(content: Array[Byte]): Response = copy(entity = Some(Entity(content)))
  def entityAsString: String = entity match {
    case Some(value) => value.toString
    case _ => throw new RuntimeException("There is no entity in this response! Consider using response.entity:Option[Entity] instead.")
  }
}
