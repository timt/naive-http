package io.shaka.http

import java.io.FileInputStream
import java.security.{KeyStore ⇒ JKeyStore}
import java.security.cert.X509Certificate
import javax.net.ssl._

object Https {
  private val defaultProtocol = "TLS"

  trait TrustStoreConfig
  case class TrustServersByTrustStore(path: String, password: String) extends TrustStoreConfig
  case object TrustAnyServer extends TrustStoreConfig

  case class HttpsConfig(trustStoreConfig: TrustStoreConfig, protocol: String = defaultProtocol)

  def sslFactory(httpsConfig: HttpsConfig): SSLSocketFactory = {
    val sslContext = SSLContext.getInstance(httpsConfig.protocol)
    sslContext.init(null, trustManagers(httpsConfig.trustStoreConfig), new java.security.SecureRandom)
    sslContext.getSocketFactory
  }

  def hostNameVerifier(trustStoreConfig: TrustStoreConfig): HostnameVerifier = trustStoreConfig match {
    case TrustServersByTrustStore(_, _) ⇒ HttpsURLConnection.getDefaultHostnameVerifier
    case TrustAnyServer ⇒ TrustAllSslCertificates.allHostsValid
  }

  private def trustManagers(trustStoreConfig: TrustStoreConfig): Array[TrustManager] = trustStoreConfig match {
    case TrustServersByTrustStore(path, password) ⇒
      val inputStream = new FileInputStream(path)
      val trustStore: JKeyStore = JKeyStore.getInstance(JKeyStore.getDefaultType)
      trustStore.load(inputStream, password.toCharArray)

      val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      trustManagerFactory.init(trustStore)
      trustManagerFactory.getTrustManagers
    case TrustAnyServer ⇒ TrustAllSslCertificates.trustAllCerts
  }

  object TrustAllSslCertificates {
    val trustAllCerts = Array[TrustManager](new X509TrustManager {
      def getAcceptedIssuers: Array[X509Certificate] = { null }
      def checkClientTrusted(certs: Array[X509Certificate], authType: String) {}
      def checkServerTrusted(certs: Array[X509Certificate], authType: String) {}
    })

    val sc = SSLContext.getInstance(defaultProtocol)
    sc.init(null, trustAllCerts, new java.security.SecureRandom)

    val allHostsValid = new HostnameVerifier {
      def verify(hostname: String, session: SSLSession) = { true }
    }

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory)
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
  }
}