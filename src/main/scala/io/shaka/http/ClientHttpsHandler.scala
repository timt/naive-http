package io.shaka.http

import io.shaka.http.Http.Url
import io.shaka.http.proxy.Proxy
import java.security.KeyStore
import javax.net.ssl.{SSLSocketFactory, HttpsURLConnection, SSLContext, TrustManagerFactory}
import java.io.{FileInputStream, InputStream}

case class HttpsKeyStore(path: String, password: String)

object ClientHttpsHandler {
  def apply(proxy: Proxy, httpsKeyStore: HttpsKeyStore): ClientHttpsHandler = {

    val keyStoreInputStream = new FileInputStream(httpsKeyStore.path)

    try {
      new ClientHttpsHandler(proxy, keyStoreInputStream, httpsKeyStore.password)
    } finally keyStoreInputStream.close()
  }
}

class ClientHttpsHandler(val proxy: Proxy, val keyStoreInputStream: InputStream, val keyStorePassword: String) extends ClientHttpHandler(proxy) {
  val sslFactory: SSLSocketFactory = {
    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)

    keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray)

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    trustManagerFactory.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagerFactory.getTrustManagers, null)
    sslContext.getSocketFactory
  }

  override protected def createConnection(url: Url, proxy: Proxy) = {
    val connection = super.createConnection(url, proxy).asInstanceOf[HttpsURLConnection]
    connection.setSSLSocketFactory(sslFactory)
    connection
  }
}