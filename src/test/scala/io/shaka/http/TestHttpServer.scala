package io.shaka.http

import io.shaka.http.Http.{Header, HttpHandler}
import io.shaka.http.Request.{GET, POST}
import io.shaka.http.RequestMatching.URLMatcher
import io.shaka.http.Response.{ok, respond}
import io.shaka.http.Status.NOT_FOUND
import io.shaka.http.TestCerts.keyStoreWithServerCert

object TestHttpServer {
  def withServer(block: TestHttpServer => Unit) = {
    val server = new TestHttpServer().start()
    block(server)
    server.stop()
  }
  def withHttpsServer(block: TestHttpServer => Unit) = {
    System.setProperty("jetty.ssl.keyStore", keyStoreWithServerCert.path)
    System.setProperty("jetty.ssl.keyStorePassword", keyStoreWithServerCert.password)
    val server = new TestHttpServer().start()
    block(server)
    server.stop()
    System.clearProperty("jetty.ssl.keyStore")
    System.clearProperty("jetty.ssl.keyStorePassword")
  }
  def apply() = new TestHttpServer().start()
}

class TestHttpServer(private val server: HttpServer = HttpServer()) {
  type ServerAssert = (RequestAssertions) => (Unit)
  private var serverAsserts = List[ServerAssert]()
  private var responseHeadersToAdd: List[Header] = Nil

  private var getResponse: (String) => Response = (path) =>
    responseHeadersToAdd.foldLeft(ok.entity(path)) { case (response, header) => response.header(header._1, header._2)}

  def get(url: String) = new {
    def responds(response: Response) {
      getResponse = (path) => if (path == url) response else respond("You're having a laugh").status(NOT_FOUND)
    }
  }

  def toUrl(path: String) = url + path

  def addAssert(assert: ServerAssert) {
    serverAsserts = assert :: serverAsserts
  }

  def addResponseHeader(header: HttpHeader, value: String) {
    responseHeadersToAdd = (header, value) :: responseHeadersToAdd
  }

  private val echo: HttpHandler = (req) => {
    val request = RequestAssertions(req)
    serverAsserts.foreach(_ (request))
    val statusAndHeaders = responseHeadersToAdd.foldLeft(ok) { case (response, header) => response.header(header._1, header._2) }
    request.body.fold(statusAndHeaders)(statusAndHeaders.entity(_))
  }

  private def start() = {
    server
      .handler{
        case req@GET(url"/$p") =>
          serverAsserts.foreach(_(RequestAssertions(req)))
          getResponse(p)
        case req@POST(url"/echoPost") =>
          echo(req)
      }
      .start()
    this
  }

  private def stop() {
    server.stop()
  }

  private def url = s"http://localhost:${server.port}/"


}