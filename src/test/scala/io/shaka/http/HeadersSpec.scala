package io.shaka.http

import org.scalatest.FunSuite
import io.shaka.http.HttpHeader.{CONTENT_TYPE, ETAG}
import io.shaka.http.ContentType.{APPLICATION_XHTML_XML, APPLICATION_JSON}

class HeadersSpec extends FunSuite {

  val headers = Headers(List(
    ETAG -> "sheep",
    ETAG -> "cheese",
    CONTENT_TYPE -> APPLICATION_JSON.value
  ))

  test("contains(Header) works") {
    assert(headers.contains(ETAG, "cheese"))
    assert(headers.contains(CONTENT_TYPE, APPLICATION_XHTML_XML.value) === false)
  }

}
