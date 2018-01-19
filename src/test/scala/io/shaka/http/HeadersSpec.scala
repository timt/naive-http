package io.shaka.http

import org.scalatest.{FunSuite, Matchers}
import io.shaka.http.HttpHeader.{CONTENT_TYPE, ETAG}
import io.shaka.http.ContentType.{APPLICATION_JSON, APPLICATION_XHTML_XML}
import io.shaka.http.Request.GET

class HeadersSpec extends FunSuite with Matchers{

  val headers = Headers(List(
    ETAG -> "sheep",
    ETAG -> "cheese",
    CONTENT_TYPE -> APPLICATION_JSON.value
  ))

  test("contains(Header) works") {
    assert(headers.contains(ETAG, "cheese"))
    assert(headers.contains(CONTENT_TYPE, APPLICATION_XHTML_XML.value) === false)
  }

  test("cookie headers work") {
    val mycookie = Cookie("mycookie", "mycookievalue")
    val request: Request = GET("/").cookie(mycookie)
    request.cookies should contain(mycookie)
  }

}
