package io.shaka.http

object ContentType {
  case object WILDCARD extends ContentType {val value = "*/*"}
  case object APPLICATION_XML extends ContentType {val value = "application/xml"}
  case object APPLICATION_ATOM_XML extends ContentType {val value = "application/atom+xml"}
  case object APPLICATION_XHTML_XML extends ContentType {val value = "application/xhtml+xml"}
  case object APPLICATION_SVG_XML extends ContentType {val value = "application/svg+xml"}
  case object APPLICATION_JAVASCRIPT extends ContentType {val value = "application/javascript"}
  case object APPLICATION_JSON extends ContentType {val value = "application/json"}
  case object APPLICATION_PDF extends ContentType {val value = "application/pdf"}
  case object APPLICATION_FORM_URLENCODED extends ContentType {val value = "application/x-www-form-urlencoded"}
  case object APPLICATION_OCTET_STREAM extends ContentType {val value = "application/octet-stream"}
  case object APPLICATION_MS_EXCEL extends ContentType {val value = "application/vnd.ms-excel"}
  case object MULTIPART_FORM_DATA extends ContentType {val value = "multipart/form-data"}
  case object TEXT_PLAIN extends ContentType {val value = "text/plain"}
  case object TEXT_CSV extends ContentType {val value = "text/csv"}
  case object TEXT_XML extends ContentType {val value = "text/xml"}
  case object TEXT_HTML extends ContentType {val value = "text/html"}
  case object TEXT_CSS extends ContentType {val value = "text/css"}
  case object TEXT_JAVASCRIPT extends ContentType {val value = "text/javascript"}
  case object TEXT_CACHE_MANIFEST extends ContentType {val value = "text/cache-manifest"}
  case object IMAGE_PNG extends ContentType {val value = "image/png"}
  case object IMAGE_GIF extends ContentType {val value = "image/gif"}
  case object IMAGE_X_ICON extends ContentType {val value = "image/x-icon"}
  case object IMAGE_JPEG extends ContentType {val value = "image/jpeg"}
  case object IMAGE_SVG extends ContentType {val value = "image/svg+xml"}
}
trait ContentType {def value: String}
