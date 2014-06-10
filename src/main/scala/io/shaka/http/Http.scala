package io.shaka.http

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http: HttpHandler = new ClientHttpHandler
  def http(proxy: Proxy): HttpHandler = new ClientHttpHandler(Some(proxy))
}
