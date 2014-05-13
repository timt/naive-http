package io.shaka.http

import unfiltered.filter.Planify
import unfiltered.request.{Body, Seg, Path}
import unfiltered.response.{ResponseHeader, Ok, NotFound, ResponseString}
import unfiltered.jetty

object TestHttpServer {

  type ServerAssert = (RequestAssertions) => (Unit)
  var serverAsserts = List[ServerAssert]()
  var responseHeaderToAdd: Option[ResponseHeader] = None

  val getEcho = Planify {
    case req@unfiltered.request.GET(Path(Seg(p :: Nil))) =>
      serverAsserts.foreach(_(RequestAssertions(req)))
      val statusAndHeaders = responseHeaderToAdd match {
        case Some(header) => Ok ~> header
        case _ => Ok
      }
      statusAndHeaders ~> ResponseString(p)
  }

  val postEcho = Planify {
    case req@unfiltered.request.POST(Path(Seg("echoPost" :: Nil))) =>
      serverAsserts.foreach(_(RequestAssertions(req)))
      val statusAndHeaders = responseHeaderToAdd match {
        case Some(header) => Ok ~> header
        case _ => Ok
      }
      statusAndHeaders ~> ResponseString(Body.string(req))

  }

  val notFound = Planify {case _ => NotFound ~> ResponseString("You're having a laugh")}

  val server: jetty.Http = unfiltered.jetty.Http.anylocal
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
    responseHeaderToAdd = None
  }

  def addAssert(assert: ServerAssert) {
    serverAsserts = assert :: serverAsserts
  }

  def addResponseHeader(header: HttpHeader, value: String) {
    responseHeaderToAdd = Some(ResponseHeader(header.name, List(value)))
  }

  def url = server.url

}
