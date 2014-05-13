package io.shaka.http

import io.shaka.http.Http._
import scala.Some

object Request {
  def POST(url: Url) = Request(Method.POST, url)

  def GET(url: Url) = Request(Method.GET, url)
}

case class Request(method: Method, url: Url, headers: Headers = Map(), entity: Option[Entity] = None) {
  def header(header: HttpHeader, value: String): Request = copy(headers = headers + (header -> value))

  def entity(content: String) = copy(entity = Some(Entity(content)))
}



