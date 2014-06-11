package io.shaka.http

import java.net.{Proxy => JavaProxy, PasswordAuthentication, Authenticator, InetSocketAddress}
import java.net.Proxy.Type._

object proxy {
  type Proxy = () => JavaProxy
  type Authentication = (String, String) => (String, String)

  val noProxy: Proxy = () => JavaProxy.NO_PROXY

  def apply(host: String, port: Int): Proxy = () => new JavaProxy(HTTP, new InetSocketAddress(host, port))

  def apply(host: String, port: Int, username: String, password: String): Proxy = {
    Authenticator.setDefault(new Authenticator() {
      override def getPasswordAuthentication = new PasswordAuthentication(username, password.toCharArray)
    })
    this(host, port)
  }

}

