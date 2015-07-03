package io.shaka.http

import io.shaka.http.HttpHeader.{ACCEPT, CONTENT_TYPE}

object RequestMatching {
  object && {
    def unapply[A](a: A) = Some((a, a))
  }

  implicit class URLMatcher(val sc: StringContext) extends AnyVal {
    def url = sc.parts.mkString("(.+)")
      .replaceAllLiterally("?", "\\?")
      .r
  }

  object Accept {
    @deprecated("Only works for exactly 1 accept header value. Instead import RequestOps and use guard condition 'if req.accepts(someContentType)'", "2015-02-04")
    def unapply(request: Request) = if (request.headers.contains(ACCEPT)) request.headers(ACCEPT).headOption.map(ContentType.contentType) else None
  }

  object HasContentType{
    def unapply(request: Request) = if (request.headers.contains(CONTENT_TYPE)) request.headers(CONTENT_TYPE).headOption.map(ContentType.contentType) else None
  }

  object Path {
    def unapply(path: String): Option[List[String]] = Some(path.split("/").toList match {
      case "" :: xs => xs
      case x => x
    })
  }

  implicit class RequestOps(request: Request) {
    def accepts(contentType: ContentType): Boolean =
      request.headers(ACCEPT).exists(_.contains(contentType.value))
  }

}
