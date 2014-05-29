package io.shaka.http

import io.shaka.http.Http._
import io.shaka.http.HttpHeader.CONTENT_TYPE
import io.shaka.http.ContentType.APPLICATION_FORM_URLENCODED
import io.shaka.http.FormParameters.{fromEntity, toEntity}
import scala.Some

object Request {
  def POST(url: Url) = Request(Method.POST, url)

  def GET(url: Url) = Request(Method.GET, url)
}

case class Request(method: Method, url: Url, headers: Headers = Headers.Empty, entity: Option[Entity] = None) {
  def formParameters(parameters: FormParameter*): Request = {
    val existingFormParameters = entity.fold(List[FormParameter]())(fromEntity)
    copy(
      entity = Some(toEntity(existingFormParameters ++ parameters)),
      headers = (CONTENT_TYPE -> APPLICATION_FORM_URLENCODED.value) :: headers
    )
  }


  def header(header: HttpHeader, value: String): Request = copy(headers = (header, value) :: headers)

  def contentType(value: String) = header(CONTENT_TYPE, value)

  def contentType(value: ContentType) = header(CONTENT_TYPE, value.value)

  def entity(content: String) = copy(entity = Some(Entity(content)))
}



