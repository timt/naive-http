package io.shaka.http

import io.shaka.http.Request.GET
import org.scalatest.FunSuite
import io.shaka.http.RequestMatching.URLMatcher


class QueryParametersSpec extends FunSuite {
  test("repeating") {
    assert(QueryParameters.unapply("") === Some(Map()))
    assert(QueryParameters.unapply("key=value") === Some(Map("key" -> List("value"))))
    assert(QueryParameters.unapply("key=value&key2=value2") === Some(Map("key" -> List("value"), "key2" -> List("value2"))))
    assert(QueryParameters.unapply("key=value&key=value2") === Some(Map("key" -> List("value", "value2"))))

    assert((GET("some/url?key=oldvalue&key2=value2&key=value") match {
      case GET(url"some/url?${QueryParameters(params)}") ⇒ params
    }) === Map("key" -> List("oldvalue", "value"), "key2" -> List("value2")))
  }
  test("encoded with & in value") {
    assert(QueryParameters.unapply("key=value1%20%26%20value2") === Some(Map("key" -> List("value1 & value2"))))
  }
}
