package io.shaka.http

import io.shaka.http.Https.{TrustAllSslCertificates, HttpsKeyStore, TrustingKeyStore}
import io.shaka.http.Request.GET
import io.shaka.http.Status.OK
import io.shaka.http.TestHttpServer.withHttpsServer
import org.scalatest.{BeforeAndAfterEach, FunSuite}


class HttpsSpec extends FunSuite with BeforeAndAfterEach {
  var server: TestHttpServer = _

  test("https works with https keystore") {
    withHttpsServer{server =>
      implicit val https = Some(HttpsKeyStore("src/test/resources/certs/keystore-testing.jks", "password"))
      val expected = "helloworld"
      val response = Http.http(GET(server.toUrl(expected)))
      assert(response.status === OK)
      assert(response.entityAsString === expected)
    }
  }

  test("https works with trusting keystore") {
    withHttpsServer{server =>
      implicit val https = Some(TrustingKeyStore("TLS"))
      val expected = "helloworld"
      val response = Http.http(GET(server.toUrl(expected)))
      assert(response.status === OK)
      assert(response.entityAsString === expected)
    }
  }

  test("TrustAllSslCertificates allow all https") {
    withHttpsServer{server =>
      val expected = "helloworld"
      TrustAllSslCertificates
      val response = Http.http(GET(server.toUrl(expected)))
      assert(response.status === OK)
      assert(response.entityAsString === expected)
    }
  }
}






