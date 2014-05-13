package io.shaka.http

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)
  type Headers = Map[HttpHeader, String]

  def http: HttpHandler = new ClientHttpHandler
}
