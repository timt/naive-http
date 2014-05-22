package io.shaka.http

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)
  type Headers = List[Header]

  def http: HttpHandler = new ClientHttpHandler
}
