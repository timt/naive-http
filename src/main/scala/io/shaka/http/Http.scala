package io.shaka.http

import proxy.{Proxy, noProxy}
import java.io.FileInputStream

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http(request: Request)(implicit proxy: Proxy = noProxy): Response = new ClientHttpHandler(proxy).apply(request)
  
  def https(keyStorePath: String, keyStorePassword: String): HttpHandler = {
    val keyStoreInputStream = new FileInputStream(keyStorePath)

    try {
      new ClientHttpsHandler(keyStoreInputStream, keyStorePassword)
    } finally keyStoreInputStream.close()
  }
}
