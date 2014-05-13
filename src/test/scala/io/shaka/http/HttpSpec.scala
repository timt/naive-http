package io.shaka.http

import org.scalatest.{BeforeAndAfterEach, BeforeAndAfterAll, Spec}
import io.shaka.http.Http.http
import io.shaka.http.Status.OK
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.HttpHeader.{CONTENT_TYPE, USER_AGENT}
import io.shaka.http.ContentType.APPLICATION_ATOM_XML

class HttpSpec extends Spec with BeforeAndAfterAll with BeforeAndAfterEach {

  def `get works`() {
    val expected = "helloworld"
    val response = http(GET(TestHttpServer.url + expected))
    assert(response.status === OK)
    assert(response.entity.get.toString === expected)
  }

  def `request sends headers`() {
    val userAgent = "mytest-agent"
    TestHttpServer.addAssert((req) => req.assertHeader(USER_AGENT, userAgent))
    http(GET(TestHttpServer.url + "withHeaders").header(USER_AGENT, userAgent))
  }

  def `response gets headers`() {
    val userAgent = "mytest-agent"
    TestHttpServer.addResponseHeader(CONTENT_TYPE, APPLICATION_ATOM_XML.value)
    val response = http(GET(TestHttpServer.url + "withHeaders").header(USER_AGENT, userAgent))
    assert(response.headers.contains(CONTENT_TYPE))
    assert(response.headers(CONTENT_TYPE) === APPLICATION_ATOM_XML.value)
  }

  def `post something works`() {
    val content = """{"foo":"bar"}"""
    val response = http(POST(TestHttpServer.url + "echoPost").entity(content))
    assert(response.status === OK)
    assert(response.entity.get.toString === content)
  }

  override protected def beforeAll(): Unit = TestHttpServer.start()

  override protected def afterAll(): Unit = TestHttpServer.stop()

  override protected def afterEach(): Unit = TestHttpServer.reset()

}




















