package io.shaka.http

trait HttpHeader {def name: String}
object HttpHeader {
  case object ACCEPT extends HttpHeader {val name = "Accept"}
  case object ACCEPT_CHARSET extends HttpHeader {val name = "Accept-Charset"}
  case object ACCEPT_ENCODING extends HttpHeader {val name = "Accept-Encoding"}
  case object ACCEPT_LANGUAGE extends HttpHeader {val name = "Accept-Language"}
  case object AUTHORIZATION extends HttpHeader {val name = "Authorization"}
  case object CACHE_CONTROL extends HttpHeader {val name = "Cache-Control"}
  case object CONTENT_ENCODING extends HttpHeader {val name = "Content-Encoding"}
  case object CONTENT_LANGUAGE extends HttpHeader {val name = "Content-Language"}
  case object CONTENT_LENGTH extends HttpHeader {val name = "Content-Length"}
  case object CONTENT_LOCATION extends HttpHeader {val name = "Content-Location"}
  case object CONTENT_TYPE extends HttpHeader {val name = "Content-Type"}
  case object Content_MD5 extends HttpHeader {val name = "Content-MD5"}
  case object DATE extends HttpHeader {val name = "Date"}
  case object ETAG extends HttpHeader {val name = "ETag"}
  case object EXPIRES extends HttpHeader {val name = "Expires"}
  case object HOST extends HttpHeader {val name = "Host"}
  case object IF_MATCH extends HttpHeader {val name = "If-Match"}
  case object IF_MODIFIED_SINCE extends HttpHeader {val name = "If-Modified-Since"}
  case object IF_NONE_MATCH extends HttpHeader {val name = "If-None-Match"}
  case object IF_UNMODIFIED_SINCE extends HttpHeader {val name = "If-Unmodified-Since"}
  case object LAST_MODIFIED extends HttpHeader {val name = "Last-Modified"}
  case object LOCATION extends HttpHeader {val name = "Location"}
  case object USER_AGENT extends HttpHeader {val name = "User-Agent"}
  case object VARY extends HttpHeader {val name = "Vary"}
  case object WWW_AUTHENTICATE extends HttpHeader {val name = "WWW-Authenticate"}
  case object COOKIE extends HttpHeader {val name = "Cookie"}
  case object SET_COOKIE extends HttpHeader {val name = "Set-Cookie"}
  case object X_FORWARDED_FOR extends HttpHeader {val name = "X-Forwarded-For"}
  case object X_FORWARDED_PROTO extends HttpHeader {val name = "X-Forwarded-Proto"}
  case object X_CORRELATION_ID extends HttpHeader {val name = "X-CorrelationID"}
  case object TRANSFER_ENCODING extends HttpHeader {val name = "Transfer-Encoding"}
  case object ACCESS_CONTROL_ALLOW_ORIGIN extends HttpHeader {val name = "Access-Control-Allow-Origin"}
  case object SERVER extends HttpHeader {val name = "Server"}

  case class unknownHeader(name: String) extends HttpHeader

  val values = List(
    ACCEPT,
    ACCEPT_CHARSET,
    ACCEPT_ENCODING,
    ACCEPT_LANGUAGE,
    AUTHORIZATION,
    CACHE_CONTROL,
    CONTENT_ENCODING,
    CONTENT_LANGUAGE,
    CONTENT_LENGTH,
    CONTENT_LOCATION,
    CONTENT_TYPE,
    Content_MD5,
    DATE,
    ETAG,
    EXPIRES,
    HOST,
    IF_MATCH,
    IF_MODIFIED_SINCE,
    IF_NONE_MATCH,
    IF_UNMODIFIED_SINCE,
    LAST_MODIFIED,
    LOCATION,
    USER_AGENT,
    VARY,
    WWW_AUTHENTICATE,
    COOKIE,
    SET_COOKIE,
    X_FORWARDED_FOR,
    X_FORWARDED_PROTO,
    X_CORRELATION_ID,
    TRANSFER_ENCODING,
    ACCESS_CONTROL_ALLOW_ORIGIN,
    SERVER
  )

  def httpHeader(name: String) = values.find(h => h.name equalsIgnoreCase name).getOrElse(unknownHeader(name))

}
