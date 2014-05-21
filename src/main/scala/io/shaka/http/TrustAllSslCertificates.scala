package io.shaka.http

import javax.net.ssl._
import java.security.cert.X509Certificate


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
