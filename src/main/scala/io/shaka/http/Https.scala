package io.shaka.http

import java.io.FileInputStream
import java.security.{KeyStore ⇒ JKeyStore}
import java.security.cert.X509Certificate
import javax.net.ssl._

object Https {

  trait KeyStore { def protocol: String }
  case class HttpsKeyStore(path: String, password: String, protocol: String = "TLS") extends KeyStore
  case class TrustingKeyStore(protocol: String = "SSL") extends KeyStore

  def sslFactory(keyStore: KeyStore): SSLSocketFactory = {
    val sslContext = SSLContext.getInstance(keyStore.protocol)
    sslContext.init(null, trustManagers(keyStore), new java.security.SecureRandom)
    sslContext.getSocketFactory
  }

  def hostNameVerifier(keyStore: KeyStore): HostnameVerifier = keyStore match {
    case _: HttpsKeyStore ⇒ HttpsURLConnection.getDefaultHostnameVerifier
    case _: TrustingKeyStore ⇒ TrustAllSslCertificates.allHostsValid
  }

  private def trustManagers(keyStore: KeyStore): Array[TrustManager] = keyStore match {
    case HttpsKeyStore(path, password, _) ⇒ {
      val keyStoreInputStream = new FileInputStream(path)
      val keyStore: JKeyStore = JKeyStore.getInstance(JKeyStore.getDefaultType)
      keyStore.load(keyStoreInputStream, password.toCharArray)

      val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      trustManagerFactory.init(keyStore)
      trustManagerFactory.getTrustManagers
    }
    case _: TrustingKeyStore ⇒ TrustAllSslCertificates.trustAllCerts
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
