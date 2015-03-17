package io.shaka.http

import io.shaka.http.Https.KeyStore
import io.shaka.http.proxy._

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)
  val infiniteTimeout = Timeout(0)

  def http(request: Request)(implicit proxy: Proxy = noProxy, keyStore: Option[KeyStore] = None, timeout: Timeout = infiniteTimeout ): Response = new ClientHttpHandler(proxy, keyStore, timeout: Timeout).apply(request)

  case class Timeout(millis: Int)
}
