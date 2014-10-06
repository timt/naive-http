package io.shaka.http

import io.shaka.http.proxy.{Proxy, noProxy}

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http(request: Request)(implicit proxy: Proxy = noProxy, https: Option[HttpsKeyStore] = None): Response = (https match {
    case None => new ClientHttpHandler(proxy)
    case Some(httpsKeyStore) => ClientHttpsHandler(proxy, httpsKeyStore)
  }).apply(request)
}