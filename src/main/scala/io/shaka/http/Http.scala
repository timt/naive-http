package io.shaka.http

import proxy.Proxy
import java.io.FileInputStream

object Http {
  type HttpHandler = (Request) => (Response)
  type Url = String
  type Header = (HttpHeader, String)

  def http: HttpHandler = new ClientHttpHandler
  def http(proxy: Proxy): HttpHandler = new ClientHttpHandler(proxy)

  def https(keyStorePath: String, keyStorePassword: String): HttpHandler = {
    val keyStoreInputStream = new FileInputStream(keyStorePath)

    try {
      new ClientHttpsHandler(keyStoreInputStream, keyStorePassword)
    } finally keyStoreInputStream.close()
  }
}