package io.shaka.http

import io.shaka.http.Status.{NOT_FOUND, OK, SEE_OTHER}
import io.shaka.http.HttpHeader.{CONTENT_TYPE, LOCATION, SET_COOKIE}

object Response {
  def respond(content: String): Response = Response().entity(content)
  def respond(content: Array[Byte]): Response = Response().entity(content)
  def seeOther(url: String): Response = respond("").status(SEE_OTHER).header(LOCATION, url)
  val ok: Response = Response().status(OK)
  val notFound: Response = Response().status(NOT_FOUND)
}

case class Response(status: Status = OK, headers: Headers = Headers.Empty, entity: Option[Entity] = None) {
  def status(newStatus: Status): Response = copy(status = newStatus)
  def header(header: HttpHeader, value: String): Response = copy(headers = (header, value) :: headers)
  def header(header: HttpHeader): Option[String] = headers.find(_._1 == header).map(_._2)
  def contentType(value: String): Response = header(CONTENT_TYPE, value)
  def contentType(value: ContentType): Response = header(CONTENT_TYPE, value.value)
  def contentType: Option[ContentType with Product with Serializable] = header(CONTENT_TYPE).map(ContentType.contentType)
  def setCookie(cookie: Cookie): Response = header(SET_COOKIE, cookie.toSetCookie)
  def entity(content: String): Response = entity(Entity(content))
  def entity(content: Array[Byte]): Response = entity(Entity(content))
  def entity(entity: Entity): Response = copy(entity = Some(entity))
  def entityAsString: String = entity match {
    case Some(value) => value.toString
    case _ => throw new RuntimeException("There is no entity in this response! Consider using response.entity:Option[Entity] instead.")
  }
}
