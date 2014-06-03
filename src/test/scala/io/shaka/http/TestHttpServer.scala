package io.shaka.http

import unfiltered.filter.Planify
import unfiltered.request.{Seg, Path}
import unfiltered.response._
import unfiltered.jetty
import io.shaka.http.RequestAssertions
import unfiltered.response.ResponseHeader
import unfiltered.response.ResponseString
import scala.collection.mutable.ListBuffer
import java.io.{FileInputStream, InputStream}

object TestHttpServer {

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

  val notFound = Planify {case _ => NotFound ~> ResponseString("You're having a laugh")}

  val server: jetty.Http = unfiltered.jetty.Http.anylocal
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

  def inputStreamToByteArray(is: InputStream): Array[Byte] = {
    val buf = ListBuffer[Byte]()
    var b = is.read()
    while (b != -1) {
      buf.append(b.byteValue)
      b = is.read()
    }
    buf.toArray
  }

}
