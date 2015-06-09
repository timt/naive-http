package io.shaka.http

import unfiltered.filter.Planify
import unfiltered.jetty.{Ssl, Server}
import unfiltered.request.{Path, Seg}
import unfiltered.response._


object TestHttpServer {
  def withServer(block: TestHttpServer => Unit) = {
    val server = new TestHttpServer().start()
    block(server)
    server.stop()
  }
  def withHttpsServer(block: TestHttpServer => Unit) = {
    val trustStore = PathAndPassword("src/test/resources/certs/truststore-testing.jks", "password")
    val keyStore = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password")
    val server = new TestHttpServer(HttpsWithMutualAuth(unfiltered.util.Port.any, "127.0.0.1", keyStore, trustStore)).start()
    block(server)
    server.stop()
  }
  def apply() = new TestHttpServer().start()
}

trait Ssl2 { self: Ssl =>
  override val keyStore: String = ""
  override val keyStorePassword: String = ""
}

case class PathAndPassword(path: String, password: String)

case class HttpsWithMutualAuth(port: Int, host: String, ks: PathAndPassword, ts: PathAndPassword) extends Server with Ssl  {
  override lazy val keyStore: String = ks.path
  override lazy val keyStorePassword: String = ks.password

  sslContextFactory.setTrustStore(ts.path)
  sslContextFactory.setTrustStorePassword(ts.password)

  type ServerBuilder = HttpsWithMutualAuth
  val url = "https://%s:%d/" format (host, port)
  def sslPort = port
  sslConn.setHost(host)
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
