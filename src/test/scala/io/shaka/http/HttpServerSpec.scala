package io.shaka.http

import io.shaka.http.ContentType._
import io.shaka.http.Http.http
import io.shaka.http.HttpHeader.{CONTENT_LENGTH, USER_AGENT}
import io.shaka.http.HttpServer.ToLog
import io.shaka.http.HttpServerSpecSupport.withHttpServer
import io.shaka.http.Request.{GET, HEAD, POST}
import io.shaka.http.Response.respond
import io.shaka.http.Status.{INTERNAL_SERVER_ERROR, NOT_FOUND}
import org.scalatest.FunSuite

import scala.collection.mutable

class HttpServerSpec extends FunSuite {

  test("httpServer works") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler(req => respond("Hello world"))
      val response = http(GET(rootUrl))
      assert(response.status === Status.OK)
      assert(response.entity.get.toString === "Hello world")
    }
  }

  test("httpServer receives GET method") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case req@GET(_) => respond("Hello world")
        case _ => respond("doh!").status(NOT_FOUND)
      }
      http(GET(rootUrl))
    }
  }

  test("httpServer receives POST method") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler { case POST(_) => respond("Hello world")}
      http(POST(rootUrl))
    }
  }

  test("httpServer receives headers") {
    withHttpServer { (httpServer, rootUrl) =>
      val userAgent = "mytest-agent"
      httpServer.handler { case req@GET(_) =>
        assert(req.headers.contains(USER_AGENT, userAgent))
        respond("Hello world")
      }
      http(GET(rootUrl).header(USER_AGENT, userAgent))
    }
  }

  test("httpServer sends headers") {
    withHttpServer { (httpServer, rootUrl) =>
      val userAgent = "mytest-agent"
      httpServer.handler { case GET(_) => respond("Hello world").header(USER_AGENT, userAgent)}
      val response = http(GET(rootUrl))
      assert(response.headers.contains(USER_AGENT, userAgent))
    }
  }

  test("can do content negotiation") {
    import io.shaka.http.RequestMatching.RequestOps
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case req@GET(_) if req.accepts(APPLICATION_JSON) => respond( """{"hello":"world"}""").contentType(APPLICATION_JSON)
        case req@GET(_) if req.accepts(APPLICATION_XML) => respond( """<hello>world</hello>""").contentType(APPLICATION_XML)
      }
      assert(http(GET(rootUrl).accept(APPLICATION_JSON)).entityAsString === """{"hello":"world"}""")
      assert(http(GET(rootUrl).accept(APPLICATION_XML)).entityAsString === """<hello>world</hello>""")
    }
  }

  test("can extract path parameters") {
    import io.shaka.http.RequestMatching.URLMatcher
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(url"/tickets/$ticketId?messageContains=$messageContains") =>
          assert(ticketId === "12")
          assert(messageContains === "foobar")
          respond( """{"hello":"world"}""")
      }
      val response = http(GET(s"$rootUrl/tickets/12?messageContains=foobar"))
      assert(response.status === Status.OK)
    }
  }

  test("report exceptions thrown in handler as INTERNAL_SERVER_ERROR's") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler(_ => throw new RuntimeException("dish ish foobared!"))
      val response = http(GET(s"$rootUrl/anything"))
      assert(response.status === INTERNAL_SERVER_ERROR)
      assert(response.entityAsString === "Server error: dish ish foobared!")
    }
  }

  test("head works") {
    withHttpServer { (httpServer, rootUrl) =>
      httpServer.handler {
        case GET(_) => respond("Hello world")
      }
      val response = http(HEAD(rootUrl))
      assert(response.status === Status.OK)
      assert(response.entity === None)
      assert(response.header(CONTENT_LENGTH) === Some("Hello world".length.toString))
    }
  }

  test("output the port the server has started on") {
    val loggedMessages = mutable.MutableList[String]()
    val captureMessage: ToLog = (s) ⇒ loggedMessages += s

    val httpServer = new HttpServer(0, captureMessage).start()

    assert(loggedMessages.exists(m ⇒ m.startsWith(s"naive-http-server started on port ${httpServer.port}")))

    httpServer.stop()
  }
}














    
