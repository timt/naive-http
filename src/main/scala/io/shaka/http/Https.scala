package io.shaka.http

import java.io.FileInputStream
import java.security.{KeyStore ⇒ JKeyStore}
import java.security.cert.X509Certificate
import javax.net.ssl._

object Https {
  private val defaultProtocol = "TLS"

  trait HttpsConfig { def protocol: String }
  case class TrustServersByTrustStore(path: String, password: String, protocol: String = defaultProtocol) extends HttpsConfig
  case class TrustAnyServer(protocol: String = defaultProtocol) extends HttpsConfig

  def sslFactory(httpsConfig: HttpsConfig): SSLSocketFactory = {
    val sslContext = SSLContext.getInstance(httpsConfig.protocol)
    sslContext.init(null, trustManagers(httpsConfig), new java.security.SecureRandom)
    sslContext.getSocketFactory
  }

  def hostNameVerifier(keyStore: HttpsConfig): HostnameVerifier = keyStore match {
    case _: TrustServersByTrustStore ⇒ HttpsURLConnection.getDefaultHostnameVerifier
    case _: TrustAnyServer ⇒ TrustAllSslCertificates.allHostsValid
  }

  private def trustManagers(keyStore: HttpsConfig): Array[TrustManager] = keyStore match {
    case TrustServersByTrustStore(path, password, _) ⇒
      val inputStream = new FileInputStream(path)
      val trustStore: JKeyStore = JKeyStore.getInstance(JKeyStore.getDefaultType)
      trustStore.load(inputStream, password.toCharArray)

      val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
      trustManagerFactory.init(trustStore)
      trustManagerFactory.getTrustManagers
    case _: TrustAnyServer ⇒ TrustAllSslCertificates.trustAllCerts
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