package io.shaka.http

import io.shaka.http.Https.HttpsConfig
import io.shaka.http.proxy._

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)
  val tenSecondTimeout = Timeout(10000)

  def http(request: Request)(implicit proxy: Proxy = noProxy, httpsConfig: Option[HttpsConfig] = None, timeout: Timeout = tenSecondTimeout ): Response = new ClientHttpHandler(proxy, httpsConfig, timeout: Timeout).apply(request)

  case class Timeout(millis: Int)
}
