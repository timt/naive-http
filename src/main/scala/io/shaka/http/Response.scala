package io.shaka.http

import io.shaka.http.Status.OK
import io.shaka.http.HttpHeader.CONTENT_TYPE

case class Response(status: Status = OK, headers: Headers = Headers.Empty, entity: Option[Entity] = None){

  def entity(content: String) = copy(entity = Some(Entity(content)))

  def header(header: HttpHeader, value: String) = copy(headers = (header, value) :: headers)

  def contentType(value: String) = header(CONTENT_TYPE, value)

  def contentType(value: ContentType) = header(CONTENT_TYPE, value.value)
}
