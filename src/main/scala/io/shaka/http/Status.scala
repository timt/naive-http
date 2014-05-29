package io.shaka.http

object Status {

  //INFORMATIONAL = set(100, 199}
  case object CONTINUE            extends Status {val code = 100; val description = "Continue"}
  case object SWITCHING_PROTOCOLS extends Status {val code = 101; val description = "Switching Protocols"}

  //SUCCESSFUL = set(200, 299}
  case object OK                            extends Status {val code = 200; val description =  "OK"}
  case object CREATED                       extends Status {val code = 201; val description =  "Created"}
  case object ACCEPTED                      extends Status {val code = 202; val description =  "Accepted"}
  case object NON_AUTHORITATIVE_INFORMATION extends Status {val code = 203; val description =  "Non-Authoritative Information"}
  case object NO_CONTENT                    extends Status {val code = 204; val description =  "No Content"}
  case object RESET_CONTENT                 extends Status {val code = 205; val description =  "Reset Content"}
  case object PARTIAL_CONTENT               extends Status {val code = 206; val description =  "Partial Content"}

  //REDIRECTION = set(300, 399}
  case object MULTIPLE_CHOICES   extends Status {val code = 300; val description = "Multiple Choices"}
  case object MOVED_PERMANENTLY  extends Status {val code = 301; val description = "Moved Permanently"}
  case object FOUND              extends Status {val code = 302; val description = "Found"}
  case object SEE_OTHER          extends Status {val code = 303; val description = "See Other"}
  case object NOT_MODIFIED       extends Status {val code = 304; val description = "Not Modified"}
  case object USE_PROXY          extends Status {val code = 305; val description = "Use Proxy"}
  case object TEMPORARY_REDIRECT extends Status {val code = 307; val description = "Temporary Redirect"}

  //CLIENT_ERROR = set(400, 499}
  case object BAD_REQUEST                     extends Status {val code = 400; val description = "Bad Request"}
  case object UNAUTHORIZED                    extends Status {val code = 401; val description =  "Unauthorized"}
  case object PAYMENT_REQUIRED                extends Status {val code = 402; val description =  "Payment Required"}
  case object FORBIDDEN                       extends Status {val code = 403; val description =  "Forbidden"}
  case object NOT_FOUND                       extends Status {val code = 404; val description =  "Not Found"}
  case object METHOD_NOT_ALLOWED              extends Status {val code = 405; val description =  "Method Not Allowed"}
  case object NOT_ACCEPTABLE                  extends Status {val code = 406; val description =  "Not Acceptable"}
  case object PROXY_AUTHENTICATION_REQUIRED   extends Status {val code = 407; val description =  "Proxy Authentication Required"}
  case object REQUEST_TIMEOUT                 extends Status {val code = 408; val description =  "Request Timeout"}
  case object CONFLICT                        extends Status {val code = 409; val description =  "Conflict"}
  case object GONE                            extends Status {val code = 410; val description =  "Gone"}
  case object LENGTH_REQUIRED                 extends Status {val code = 411; val description =  "Length Required"}
  case object PRECONDITION_FAILED             extends Status {val code = 412; val description =  "Precondition Failed"}
  case object REQUEST_ENTITY_TOO_LARGE        extends Status {val code = 413; val description =  "Request Entity Too Large"}
  case object REQUEST_URI_TOO_LONG            extends Status {val code = 414; val description =  "Request-URI Too Long"}
  case object UNSUPPORTED_MEDIA_TYPE          extends Status {val code = 415; val description =  "Unsupported Media Type"}
  case object REQUESTED_RANGE_NOT_SATISFIABLE extends Status {val code = 416; val description =  "Requested Range Not Satisfiable"}
  case object EXPECTATION_FAILED              extends Status {val code = 417; val description =  "Expectation Failed"}

  //SERVER_ERROR = set(500, 599}
  case object INTERNAL_SERVER_ERROR      extends Status { val code = 500; val description = "Internal Server Error"}
  case object NOT_IMPLEMENTED            extends Status { val code = 501; val description = "Not Implemented"}
  case object BAD_GATEWAY                extends Status { val code = 502; val description = "Bad Gateway"}
  case object SERVICE_UNAVAILABLE        extends Status { val code = 503; val description = "Service Unavailable"}
  case object GATEWAY_TIMEOUT            extends Status { val code = 504; val description = "Gateway Timeout"}
  case object HTTP_VERSION_NOT_SUPPORTED extends Status { val code = 505; val description = "HTTP Version Not Supported"}

  case class unknownStatus(code: Int, description: String) extends Status
  val values = List(
    CONTINUE,
    SWITCHING_PROTOCOLS,
    OK,
    CREATED,
    ACCEPTED,
    NON_AUTHORITATIVE_INFORMATION,
    NO_CONTENT,
    RESET_CONTENT,
    PARTIAL_CONTENT,
    MULTIPLE_CHOICES,
    MOVED_PERMANENTLY,
    FOUND,
    SEE_OTHER,
    NOT_MODIFIED,
    USE_PROXY,
    TEMPORARY_REDIRECT,
    BAD_REQUEST,
    UNAUTHORIZED,
    PAYMENT_REQUIRED,
    FORBIDDEN,
    NOT_FOUND,
    METHOD_NOT_ALLOWED,
    NOT_ACCEPTABLE,
    PROXY_AUTHENTICATION_REQUIRED,
    REQUEST_TIMEOUT,
    CONFLICT,
    GONE,
    LENGTH_REQUIRED,
    PRECONDITION_FAILED,
    REQUEST_ENTITY_TOO_LARGE,
    REQUEST_URI_TOO_LONG,
    UNSUPPORTED_MEDIA_TYPE,
    REQUESTED_RANGE_NOT_SATISFIABLE,
    EXPECTATION_FAILED,
    INTERNAL_SERVER_ERROR,
    NOT_IMPLEMENTED,
    BAD_GATEWAY,
    SERVICE_UNAVAILABLE,
    GATEWAY_TIMEOUT,
    HTTP_VERSION_NOT_SUPPORTED
  )

  def status(code: Int, description: String) = values.find(s => s.code == code && s.description == description).getOrElse(unknownStatus(code,description))
}

sealed trait Status {
  def code: Int
  def description: String
}