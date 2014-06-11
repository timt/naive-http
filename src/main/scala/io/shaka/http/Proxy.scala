package io.shaka.http

import java.net.{Proxy => JavaProxy, PasswordAuthentication, Authenticator, InetSocketAddress}
import java.net.Proxy.Type._

object Proxy {
  type Proxy = () => JavaProxy
  type Authentication = (String, String) => (String, String)

  val noProxy: Proxy = () => JavaProxy.NO_PROXY

  def proxy(host: String, port: Int): Proxy = () => new JavaProxy(HTTP, new InetSocketAddress(host, port))

  def proxy(host: String, port: Int, username: String, password: String): Proxy = {
    Authenticator.setDefault(new Authenticator() {
      override def getPasswordAuthentication = new PasswordAuthentication(username, password.toCharArray)
    })
    proxy(host, port)
  }

}

