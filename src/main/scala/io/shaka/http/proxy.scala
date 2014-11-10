package io.shaka.http

import java.net.Proxy.Type._
import java.net.{Authenticator, InetSocketAddress, PasswordAuthentication, Proxy => JavaProxy}

object proxy {
  type Proxy = () => JavaProxy
  type Credentials = (String, String)

  val noProxy: Proxy = () => JavaProxy.NO_PROXY

  def credentials(username: String, password: String): Credentials = (username, password)

  def apply(host: String, port: Int, credentials: Option[Credentials] = None): Proxy = () => {
    credentials.foreach{ case (username, password) =>
      Authenticator.setDefault(new Authenticator() {
        override def getPasswordAuthentication = new PasswordAuthentication(username, password.toCharArray)
      })
    }
    new JavaProxy(HTTP, new InetSocketAddress(host, port))
  }

  def apply(host: String, port: Int, username: String, password: String): Proxy = {
    this(host, port, Some(credentials(username,password)))
  }

}

