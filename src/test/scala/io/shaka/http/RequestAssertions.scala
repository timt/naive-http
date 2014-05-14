package io.shaka.http

import unfiltered.request.{Body, HttpRequest}

case class RequestAssertions(request: HttpRequest[Any]) {
  val body = Body.string(request)


  import org.scalatest.Matchers._

  def assertHeader(key: HttpHeader, value: String) {
    request.headers(key.name).foreach(value => println(s"${key.name}:$value"))
    assert(request.headerNames.contains(key.name), s"request did not contain $key header")
    request.headers(key.name).foreach {
      actual =>
        assert(actual === value)
    }
  }

  def assertEntity(entity: String) {
    assert(body === entity)
  }
}
