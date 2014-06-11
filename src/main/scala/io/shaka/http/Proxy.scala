package io.shaka.http

import java.net.{InetSocketAddress, Proxy => JavaProxy}
import java.net.Proxy.Type._

object Proxy{
  type Proxy =  () => JavaProxy

  val noProxy:Proxy = () => JavaProxy.NO_PROXY

  def proxy(host: String, port: Int): Proxy = () => new JavaProxy(HTTP, new InetSocketAddress(host,port))

}

