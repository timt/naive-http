package io.shaka.http

import proxy.Proxy

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http: HttpHandler = new ClientHttpHandler
  def http(proxy: Proxy): HttpHandler = new ClientHttpHandler(proxy)
}
