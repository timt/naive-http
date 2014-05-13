package io.shaka.http

import unfiltered.request.{Body, HttpRequest}

case class RequestAssertions(request: HttpRequest[Any]) {

  import org.scalatest.Matchers._

  def assertHeader(key: HttpHeader, value: String) {
    assert(request.headerNames.contains(key.name), s"request did not contain $key header")
    request.headers(key.name).foreach {
      actual =>
        assert(actual === value)
    }
  }

  def assertEntity(entity: String) {
    assert(Body.string(request) === entity)
  }
}
