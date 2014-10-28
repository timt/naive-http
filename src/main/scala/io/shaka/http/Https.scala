package io.shaka.http

import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.X509Certificate
import javax.net.ssl._

object Https {

  case class HttpsKeyStore(path: String, password: String)

  def sslFactory(httpsKeyStore: HttpsKeyStore) = {
    val keyStoreInputStream = new FileInputStream(httpsKeyStore.path)
    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType)

    keyStore.load(keyStoreInputStream, httpsKeyStore.password.toCharArray)

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    trustManagerFactory.init(keyStore)

    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, trustManagerFactory.getTrustManagers, null)
    sslContext.getSocketFactory
  }

  object TrustAllSslCertificates {
    val trustAllCerts = Array[TrustManager](new X509TrustManager {
      def getAcceptedIssuers: Array[X509Certificate] = { null }
      def checkClientTrusted(certs: Array[X509Certificate], authType: String) {}
      def checkServerTrusted(certs: Array[X509Certificate], authType: String) {}
    })

    val sc = SSLContext.getInstance("SSL")
    sc.init(null, trustAllCerts, new java.security.SecureRandom)

    val allHostsValid = new HostnameVerifier {
      def verify(hostname: String, session: SSLSession) = { true }
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
  }


}
