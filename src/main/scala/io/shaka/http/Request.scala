package io.shaka.http

import io.shaka.http.Http._
import scala.Some
import io.shaka.http.HttpHeader.CONTENT_TYPE
import io.shaka.http.ContentType.APPLICATION_FORM_URLENCODED

object Request {
  def POST(url: Url) = Request(Method.POST, url)

  def GET(url: Url) = Request(Method.GET, url)
}

case class Request(method: Method, url: Url, headers: Headers = Map(), entity: Option[Entity] = None) {
  def formParameters(parameters: FormParameter*): Request = {
    val existingFormParameters = entity.map(FormParameters.fromEntity).getOrElse(List())
    copy(
      entity = Some(FormParameters.toEntity(existingFormParameters ++ parameters)),
      headers = headers + (CONTENT_TYPE -> APPLICATION_FORM_URLENCODED.value)
    )
  }


  def header(header: HttpHeader, value: String): Request = copy(headers = headers + (header -> value))

  def entity(content: String) = copy(entity = Some(Entity(content)))
}



