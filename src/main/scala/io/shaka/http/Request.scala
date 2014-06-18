package io.shaka.http

import io.shaka.http.Http._
import io.shaka.http.HttpHeader.CONTENT_TYPE
import io.shaka.http.ContentType.APPLICATION_FORM_URLENCODED
import io.shaka.http.FormParameters.{fromEntity, toEntity}
import scala.Some


object Request {

  object GET {
    def apply(url: Url) = Request(Method.GET, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.GET) Some(req.url) else None
  }

  object POST {
    def apply(url: Url) = Request(Method.POST, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.POST) Some(req.url) else None
  }

  object PUT {
    def apply(url: Url) = Request(Method.PUT, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.PUT) Some(req.url) else None
  }

  object HEAD {
    def apply(url: Url) = Request(Method.HEAD, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.HEAD) Some(req.url) else None
  }

  object DELETE {
    def apply(url: Url) = Request(Method.DELETE, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.DELETE) Some(req.url) else None
  }
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



