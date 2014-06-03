package io.shaka.http

import org.scalatest.{Matchers, BeforeAndAfterEach, BeforeAndAfterAll, Spec}
import io.shaka.http.Http.http
import io.shaka.http.Status.OK
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.HttpHeader.{ETAG, CONTENT_TYPE, USER_AGENT}
import io.shaka.http.ContentType.APPLICATION_ATOM_XML
import io.shaka.http.FormParameters.FormParameters
import Matchers._

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
    assert(response.headers.contains(CONTENT_TYPE, APPLICATION_ATOM_XML.value))
  }

  def `response gets headers with many values`() {
    val userAgent = "mytest-agent"
    TestHttpServer.addResponseHeader(ETAG, "sheep")
    TestHttpServer.addResponseHeader(ETAG, "cheese")
    val response = http(GET(TestHttpServer.url + "withHeaders").header(USER_AGENT, userAgent))
    assert(response.headers(ETAG) === List("sheep", "cheese"))
  }

  def `post something works`() {
    val content = """{"foo":"bar"}"""
    val response = http(POST(TestHttpServer.url + "echoPost").entity(content))
    assert(response.status === OK)
    assert(response.entity.get.toString === content)
  }

  def `can post form parameters`() {
    val formParameters: FormParameters = List(FormParameter("name1", "some value"), FormParameter("name2"))
    TestHttpServer.addAssert((req) => req.assertHeader(CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.value))
    TestHttpServer.addAssert((req) => req.assertEntity("name1=some+value&name2"))

    val response = http(POST(TestHttpServer.url + "echoPost")
      .formParameters(formParameters:_*))
    assert(response.status === OK)
    assert(response.entity.get.toString === "name1=some+value&name2")
  }

  def `can get pdf`(){
    val response = http(GET(TestHttpServer.url + "somepdf"))
    assert(response.status === OK)
    response.entity should not be empty
  }

  def `empty response entity represented as None`() {
    assert(http(GET(TestHttpServer.url + "empty")).entity === None)
  }

  override protected def beforeAll(): Unit = TestHttpServer.start()

  override protected def afterAll(): Unit = TestHttpServer.stop()

  override protected def afterEach(): Unit = TestHttpServer.reset()

}




















