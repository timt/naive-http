package io.shaka.http

import io.shaka.http.Http.Url
import io.shaka.http.proxy.Proxy
import java.security.KeyStore
import javax.net.ssl.{SSLSocketFactory, HttpsURLConnection, SSLContext, TrustManagerFactory}
import java.io.InputStream


class ClientHttpsHandler(keyStoreInputStream: InputStream, keyStorePassword: String) extends ClientHttpHandler {
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