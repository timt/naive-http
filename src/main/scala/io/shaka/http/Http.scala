package io.shaka.http

import io.shaka.http.proxy._

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http(request: Request)(implicit proxy: Proxy = noProxy): Response = new ClientHttpHandler(proxy).apply(request)
}
