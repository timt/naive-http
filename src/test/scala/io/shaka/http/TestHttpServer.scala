package io.shaka.http

import javax.servlet.http.HttpServletRequest

import unfiltered.filter.Planify
import unfiltered.request.{HttpRequest, Seg, Path}
import unfiltered.response._
import unfiltered.jetty
import unfiltered.response.ResponseHeader
import unfiltered.response.ResponseString
import java.io.FileInputStream
import IO.inputStreamToByteArray


object TestHttpServer {
  def withServer(block: TestHttpServer => Unit) = {
    val server = new TestHttpServer().start()
    block(server)
    server.stop()
  }
  def withHttpsServer(block: TestHttpServer => Unit) = {
    System.setProperty("jetty.ssl.keyStore", "src/test/resources/certs/keystore-testing.jks")
    System.setProperty("jetty.ssl.keyStorePassword", "password")
    val server = new TestHttpServer(unfiltered.jetty.Https.anylocal).start()
    block(server)
    server.stop()
    System.clearProperty("jetty.ssl.keyStore")
    System.clearProperty("jetty.ssl.keyStorePassword")
  }
  def apply() = new TestHttpServer().start()
}

class TestHttpServer(private val server:unfiltered.jetty.Server = unfiltered.jetty.Http.anylocal) {
  type ServerAssert = (RequestAssertions) => (Unit)
  private var serverAsserts = List[ServerAssert]()
  private var responseHeadersToAdd: List[ResponseHeader] = Nil

  private var getResponse: (String) => ResponseFunction[Any] = (path) => {
    val statusAndHeaders = responseHeadersToAdd.foldLeft(Ok: ResponseFunction[Any]) { case (status, header) => status ~> header}
    statusAndHeaders ~> ResponseString(path)
  }

  def get(url: String) = new {
    def responds(response: ResponseFunction[Any]) {
      getResponse = (path) => if (path == url) response else NotFound ~> ResponseString("You're having a laugh")
    }
  }

  def toUrl(path: String) = url + path

  def addAssert(assert: ServerAssert) {
    serverAsserts = assert :: serverAsserts
  }

  def addResponseHeader(header: HttpHeader, value: String) {
    responseHeadersToAdd = ResponseHeader(header.name, List(value)) :: responseHeadersToAdd
  }

  private val getAll = Planify {
    case req@unfiltered.request.GET(Path(Seg(p :: Nil))) =>
      serverAsserts.foreach(_(RequestAssertions(req)))
      getResponse(p)
  }

  private val postEcho = Planify {
    case req@unfiltered.request.POST(Path(Seg("echoPost" :: Nil))) =>
      val request = RequestAssertions(req)
      serverAsserts.foreach(_(request))
      val statusAndHeaders = responseHeadersToAdd.foldLeft(Ok: ResponseFunction[Any]) { case (status, header) => status ~> header}
      statusAndHeaders ~> ResponseString(request.body)

  }

  private def start() = {
    server
      .filter(getAll)
      .filter(postEcho)
      .start()
    this
  }

  private def stop() {
    server.stop()
  }

  private def url = server.url


}
