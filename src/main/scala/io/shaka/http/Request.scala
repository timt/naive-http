package io.shaka.http

import javax.xml.bind.DatatypeConverter.printBase64Binary

import io.shaka.http.ContentType.APPLICATION_FORM_URLENCODED
import io.shaka.http.FormParameters.{fromEntity, toEntity}
import io.shaka.http.Http._
import io.shaka.http.HttpHeader.{COOKIE, ACCEPT, AUTHORIZATION, CONTENT_TYPE}


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

  object OPTIONS {
    def apply(url: Url) = Request(Method.OPTIONS, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.OPTIONS) Some(req.url) else None
  }

  object TRACE {
    def apply(url: Url) = Request(Method.TRACE, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.TRACE) Some(req.url) else None
  }

  object CONNECT {
    def apply(url: Url) = Request(Method.CONNECT, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.CONNECT) Some(req.url) else None
  }

  object PATCH {
    def apply(url: Url) = Request(Method.PATCH, url)

    def unapply(req: Request): Option[String] = if (req.method == Method.PATCH) Some(req.url) else None
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
  def contentType(value: String): Request = header(CONTENT_TYPE, value)
  def contentType(value: ContentType): Request = header(CONTENT_TYPE, value.value)
  def accept(value: ContentType): Request = header(ACCEPT, value.value)
  def entity(content: String): Request = copy(entity = Some(Entity(content)))
  def entity(content: Array[Byte]): Request = copy(entity = Some(Entity(content)))
  def entityAsString: String = entity match {
    case Some(value) => value.toString
    case _ => throw new RuntimeException("There is no entity in this request! Consider using request.entity:Option[Entity] instead.")
  }
  def basicAuth(user: String, password: String): Request = {
    header(AUTHORIZATION, "Basic " + printBase64Binary(s"$user:$password".getBytes))
  }
  def cookie(cookie: Cookie): Request = header(COOKIE, cookie.toSetCookie)

  def cookies: Set[Cookie] = headers.filter(_._1 == COOKIE).flatMap(_._2.split(";"))
    .map(_.trim.split("="))
    .map(cookie => Cookie(cookie.head, cookie.last))
    .toSet
}



