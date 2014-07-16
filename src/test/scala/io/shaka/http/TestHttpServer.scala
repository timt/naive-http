package io.shaka.http

import unfiltered.filter.Planify
import unfiltered.request.{Seg, Path}
import unfiltered.response._
import unfiltered.response.ResponseHeader
import unfiltered.response.ResponseString
import java.io.FileInputStream
import IO.inputStreamToByteArray
import unfiltered.jetty.Https

class TestHttpServer(unfilteredServer: =>unfiltered.jetty.Server) {

  type ServerAssert = (RequestAssertions) => (Unit)
  var serverAsserts = List[ServerAssert]()
  var responseHeadersToAdd: List[ResponseHeader] = Nil

  val getEcho = Planify {
    case req@unfiltered.request.GET(Path(Seg(p :: Nil))) =>
      req.headers("Content-Type").foreach(println)
      val request = RequestAssertions(req)
      serverAsserts.foreach(_(request))
      val statusAndHeaders = responseHeadersToAdd.foldLeft(Ok: ResponseFunction[Any]){case (status, header) => status ~> header}
      statusAndHeaders ~> ResponseString(if(p=="empty") "" else p)
  }

  val postEcho = Planify {
    case req@unfiltered.request.POST(Path(Seg("echoPost" :: Nil))) =>
      req.headers("Content-Type").foreach(println)
      val request = RequestAssertions(req)
      serverAsserts.foreach(_(request))
      val statusAndHeaders = responseHeadersToAdd.foldLeft(Ok: ResponseFunction[Any]){case (status, header) => status ~> header}
      statusAndHeaders ~> ResponseString(request.body)

  }

  val somePdfFile = "./src/test/scala/io/shaka/http/pdf-sample.pdf"
  val getPdf = Planify {
    case req@unfiltered.request.GET(Path(Seg("somepdf" :: Nil))) =>
      val is = new FileInputStream(somePdfFile)
      val bytes = inputStreamToByteArray(is)
      is.close()
      Ok ~> ResponseBytes(bytes)
  }

  val forbidden = Planify {
    case req@unfiltered.request.GET(Path(Seg("forbidden" :: Nil)))=>
      Forbidden
  }

  val notFound = Planify {case _ => NotFound ~> ResponseString("You're having a laugh")}

  val server: unfiltered.jetty.Server = unfilteredServer
    .filter(forbidden)
    .filter(getPdf)
    .filter(getEcho)
    .filter(postEcho)
    .filter(notFound)

  def start() {
    server.start()
  }

  def stop() {
    server.stop()
  }

  def reset() {
    serverAsserts = List()
    responseHeadersToAdd = List()
  }

  def addAssert(assert: ServerAssert) {
    serverAsserts = assert :: serverAsserts
  }

  def addResponseHeader(header: HttpHeader, value: String) {
    responseHeadersToAdd = ResponseHeader(header.name, List(value)) :: responseHeadersToAdd
  }

  def url = server.url
}

object TestHttpServer {
  def startHttp() = {
    val server = new TestHttpServer(unfiltered.jetty.Http.anylocal)
    server.start()
    server
  }

  def startHttps(ks: String, ksp: String) = {
    val server = new TestHttpServer(new Https(unfiltered.util.Port.any, "127.0.0.1") {
      override lazy val keyStore: String = ks
      override lazy val keyStorePassword: String = ksp
    })
    server.start()
    server
  }
}