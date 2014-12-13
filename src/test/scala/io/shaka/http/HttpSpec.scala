package io.shaka.http

import java.io.FileInputStream

import io.shaka.http.ContentType.APPLICATION_ATOM_XML
import io.shaka.http.FormParameters.FormParameters
import io.shaka.http.Http.{Timeout, http}
import io.shaka.http.HttpHeader.{CONTENT_TYPE, ETAG, USER_AGENT}
import io.shaka.http.IO._
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.Status.{FORBIDDEN, OK}
import io.shaka.http.TestHttpServer.withServer
import org.scalatest.Matchers._
import org.scalatest._
import unfiltered.response.{ResponseBytes, Forbidden, Ok}

class HttpSpec extends FunSuite with BeforeAndAfterAll with BeforeAndAfterEach {

  test("get works") {
    withServer{ server =>
      val response = http(GET(server.toUrl("ididthis")))
      assert(response.status === OK)
      assert(response.entityAsString === "ididthis")
    }
  }

  test("request sends headers") {
    withServer{ server =>
      val userAgent = "mytest-agent"
      server.addAssert((req) => req.assertHeader(USER_AGENT, userAgent))
      val response = http(GET(server.toUrl("withHeaders")).header(USER_AGENT, userAgent))
      assert(response.status === OK)
    }
  }

  test("response gets headers") {
    withServer{server =>
      server.addResponseHeader(CONTENT_TYPE, APPLICATION_ATOM_XML.value)
      val response = http(GET(server.toUrl("returnsHeaders")))
      assert(response.headers.contains(CONTENT_TYPE, APPLICATION_ATOM_XML.value))
    }
  }

  test("response gets headers with many values") {
    withServer{ server =>
      server.addResponseHeader(ETAG, "sheep")
      server.addResponseHeader(ETAG, "cheese")
      val response = http(GET(server.toUrl("returnManyHeaders")))
      assert(response.headers(ETAG) === List("sheep", "cheese"))
    }
  }

  test("post something works") {
    withServer{ server =>
      val content = """{"foo":"bar"}"""
      val response = http(POST(server.toUrl("echoPost")).entity(content))
      assert(response.status === OK)
      assert(response.entityAsString === content)
    }
  }

  test("can post form parameters") {
    withServer{server =>
      val formParameters: FormParameters = List(FormParameter("name1", "some value"), FormParameter("name2"))
      server.addAssert((req) => req.assertHeader(CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.value))
      server.addAssert((req) => req.assertEntity("name1=some+value&name2"))

      val response = http(POST(server.toUrl("echoPost"))
        .formParameters(formParameters:_*))
      assert(response.status === OK)
      assert(response.entityAsString === "name1=some+value&name2")
    }
  }

  test("can get pdf") {
    val somePdfFile = "./src/test/scala/io/shaka/http/pdf-sample.pdf"
    val is = new FileInputStream(somePdfFile)
    val bytes = inputStreamToByteArray(is)
    is.close()
    withServer { server =>
      server.get("somepdf").responds(Ok ~> ResponseBytes(bytes))
      val response = http(GET(server.toUrl("somepdf")))
      assert(response.status === OK)
      assert(response.entity.get.content === bytes)
    }
  }

  test("can handle Forbidden (connection input stream is null)") {
    withServer { server =>
      server.get("forbidden").responds(Forbidden)
      val response = http(GET(server.toUrl("forbidden")))
      assert(response.status === FORBIDDEN)
      response.entity shouldBe empty
    }
  }

  test("empty response entity represented as None") {
    withServer { server =>
      server.get("empty").responds(Ok)
      assert(http(GET(server.toUrl("empty"))).entity === None)
    }
  }

}