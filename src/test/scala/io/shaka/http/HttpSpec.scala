package io.shaka.http

import org.scalatest._
import io.shaka.http.Http.http
import io.shaka.http.Status.{FORBIDDEN, OK}
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.HttpHeader.{ETAG, CONTENT_TYPE, USER_AGENT}
import io.shaka.http.ContentType.APPLICATION_ATOM_XML
import io.shaka.http.FormParameters.FormParameters
import Matchers._

class HttpSpec extends FunSuite with BeforeAndAfterAll with BeforeAndAfterEach {
  var server: TestHttpServer = _

  test("get works") {
    val expected = "helloworld"
    val response = http(GET(server.url + expected))
    assert(response.status === OK)
    assert(response.entityAsString === expected)
  }

  test("request sends headers") {
    val userAgent = "mytest-agent"
    server.addAssert((req) => req.assertHeader(USER_AGENT, userAgent))
    http(GET(server.url + "withHeaders").header(USER_AGENT, userAgent))
  }

  test("response gets headers") {
    val userAgent = "mytest-agent"
    server.addResponseHeader(CONTENT_TYPE, APPLICATION_ATOM_XML.value)
    val response = http(GET(server.url + "withHeaders").header(USER_AGENT, userAgent))
    assert(response.headers.contains(CONTENT_TYPE, APPLICATION_ATOM_XML.value))
  }

  test("response gets headers with many values") {
    val userAgent = "mytest-agent"
    server.addResponseHeader(ETAG, "sheep")
    server.addResponseHeader(ETAG, "cheese")
    val response = http(GET(server.url + "withHeaders").header(USER_AGENT, userAgent))
    assert(response.headers(ETAG) === List("sheep", "cheese"))
  }

  test("post something works") {
    val content = """{"foo":"bar"}"""
    val response = http(POST(server.url + "echoPost").entity(content))
    assert(response.status === OK)
    assert(response.entityAsString === content)
  }

  test("can post form parameters") {
    val formParameters: FormParameters = List(FormParameter("name1", "some value"), FormParameter("name2"))
    server.addAssert((req) => req.assertHeader(CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.value))
    server.addAssert((req) => req.assertEntity("name1=some+value&name2"))

    val response = http(POST(server.url + "echoPost")
      .formParameters(formParameters:_*))
    assert(response.status === OK)
    assert(response.entityAsString === "name1=some+value&name2")
  }

  test("can get pdf") {
    val response = http(GET(server.url + "somepdf"))
    assert(response.status === OK)
    response.entity should not be empty
  }

  test("can handle Forbidden (connection input stream is null)") {
    val response = http(GET(server.url + "forbidden"))
    assert(response.status === FORBIDDEN)
    response.entity shouldBe empty
  }

  test("empty response entity represented as None") {
    assert(http(GET(server.url + "empty")).entity === None)
  }

  override protected def beforeAll(){server = TestHttpServer.startHttp()}
  override protected def afterAll(): Unit = server.stop()
  override protected def afterEach(): Unit = server.reset()
}