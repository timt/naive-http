package io.shaka.http

trait HttpHeader {def name: String}
object HttpHeader {
  case object ACCEPT extends HttpHeader {val name = "Accept"}
  case object ACCEPT_CHARSET extends HttpHeader {val name = "Accept-Charset"}
  case object ACCEPT_ENCODING extends HttpHeader {val name = "Accept-Encoding"}
  case object ACCEPT_LANGUAGE extends HttpHeader {val name = "Accept-Language"}
  case object ACCESS_CONTROL_ALLOW_HEADERS extends HttpHeader { val name = "Access-Control-Allow-Headers" }
  case object ACCESS_CONTROL_ALLOW_METHODS extends HttpHeader { val name = "Access-Control-Allow-Methods" }
  case object ACCESS_CONTROL_ALLOW_ORIGIN extends HttpHeader {val name = "Access-Control-Allow-Origin"}
  case object AUTHORIZATION extends HttpHeader {val name = "Authorization"}
  case object CACHE_CONTROL extends HttpHeader {val name = "Cache-Control"}
  case object COOKIE extends HttpHeader {val name = "Cookie"}
  case object CONTENT_ENCODING extends HttpHeader {val name = "Content-Encoding"}
  case object CONTENT_DISPOSITION extends HttpHeader {val name = "Content-Disposition"}
  case object CONTENT_LANGUAGE extends HttpHeader {val name = "Content-Language"}
  case object CONTENT_LENGTH extends HttpHeader {val name = "Content-Length"}
  case object CONTENT_LOCATION extends HttpHeader {val name = "Content-Location"}
  case object CONTENT_SECURITY_POLICY extends HttpHeader {val name = "Content-Security-Policy"}
  case object CONTENT_TYPE extends HttpHeader {val name = "Content-Type"}
  case object Content_MD5 extends HttpHeader {val name = "Content-MD5"}
  case object DATE extends HttpHeader {val name = "Date"}
  case object ETAG extends HttpHeader {val name = "ETag"}
  case object EXPIRES extends HttpHeader {val name = "Expires"}
  case object FEATURE_POLICY extends HttpHeader {val name = "Feature-Policy"}
  case object HOST extends HttpHeader {val name = "Host"}
  case object IF_MATCH extends HttpHeader {val name = "If-Match"}
  case object IF_MODIFIED_SINCE extends HttpHeader {val name = "If-Modified-Since"}
  case object IF_NONE_MATCH extends HttpHeader {val name = "If-None-Match"}
  case object IF_UNMODIFIED_SINCE extends HttpHeader {val name = "If-Unmodified-Since"}
  case object LAST_MODIFIED extends HttpHeader {val name = "Last-Modified"}
  case object LOCATION extends HttpHeader {val name = "Location"}
  case object ORIGIN extends HttpHeader {val name = "Origin"}
  case object PRAGMA extends HttpHeader {val name = "Pragma"}
  case object SERVER extends HttpHeader {val name = "Server"}
  case object SET_COOKIE extends HttpHeader {val name = "Set-Cookie"}
  case object STRICT_TRANSPORT_SECURITY extends HttpHeader {val name = "Strict-Transport-Security"}
  case object TRANSFER_ENCODING extends HttpHeader {val name = "Transfer-Encoding"}
  case object USER_AGENT extends HttpHeader {val name = "User-Agent"}
  case object VARY extends HttpHeader {val name = "Vary"}
  case object WWW_AUTHENTICATE extends HttpHeader {val name = "WWW-Authenticate"}
  case object X_VIA extends HttpHeader {val name = "X-Via"}
  case object X_FORWARDED_FOR extends HttpHeader {val name = "X-Forwarded-For"}
  case object X_FORWARDED_PROTO extends HttpHeader {val name = "X-Forwarded-Proto"}
  case object X_CORRELATION_ID extends HttpHeader {val name = "X-CorrelationID"}



  case class unknownHeader(name: String) extends HttpHeader

  val values = List(
    ACCEPT,
    ACCEPT_CHARSET,
    ACCEPT_ENCODING,
    ACCEPT_LANGUAGE,
    ACCESS_CONTROL_ALLOW_HEADERS,
    ACCESS_CONTROL_ALLOW_METHODS,
    ACCESS_CONTROL_ALLOW_ORIGIN,
    AUTHORIZATION,
    CACHE_CONTROL,
    CONTENT_DISPOSITION,
    CONTENT_ENCODING,
    CONTENT_LANGUAGE,
    CONTENT_LENGTH,
    CONTENT_LOCATION,
    CONTENT_SECURITY_POLICY,
    CONTENT_TYPE,
    Content_MD5,
    DATE,
    ETAG,
    EXPIRES,
    FEATURE_POLICY,
    HOST,
    IF_MATCH,
    IF_MODIFIED_SINCE,
    IF_NONE_MATCH,
    IF_UNMODIFIED_SINCE,
    LAST_MODIFIED,
    LOCATION,
    ORIGIN,
    PRAGMA,
    USER_AGENT,
    VARY,
    WWW_AUTHENTICATE,
    COOKIE,
    SET_COOKIE,
    STRICT_TRANSPORT_SECURITY,
    X_FORWARDED_FOR,
    X_FORWARDED_PROTO,
    X_CORRELATION_ID,
    X_VIA,
    TRANSFER_ENCODING,
    SERVER
  )

  def httpHeader(name: String) = values.find(h => h.name equalsIgnoreCase name).getOrElse(unknownHeader(name))

}
