package io.shaka.http

import io.shaka.http.Https.{HttpsConfig, TrustAllSslCertificates, TrustServersByTrustStore, TrustAnyServer}
import io.shaka.http.Request.GET
import io.shaka.http.Status.OK
import io.shaka.http.TestHttpServer.withHttpsServer
import io.shaka.http.TestHttpServer.withServer
import org.scalatest.{BeforeAndAfterEach, FunSuite}


class HttpsSpec extends FunSuite with BeforeAndAfterEach {
  var server: TestHttpServer = _

  test("https works with https keystore") {
    withHttpsServer{server =>
      implicit val https: Option[HttpsConfig] = Some(HttpsConfig(TrustServersByTrustStore("src/test/resources/certs/keystore-testing.jks", "password")))
      val expected = "helloworld"
      val response = Http.http(GET(server.toUrl(expected)))
      assert(response.status === OK)
      assert(response.entityAsString === expected)
    }
  }

  test("http ignores https keystore") {
    withServer{server =>
      implicit val https: Option[HttpsConfig] = Some(HttpsConfig(TrustServersByTrustStore("src/test/resources/certs/keystore-testing.jks", "password")))
      val expected = "helloworld"
      val url: String = server.toUrl(expected)
      assert(url.startsWith("http://"))
      val response = Http.http(GET(url))
      assert(response.status === OK)
      assert(response.entityAsString === expected)
    }
  }

  test("https works with trusting keystore") {
    withHttpsServer{server =>
      implicit val https: Option[HttpsConfig] = Some(HttpsConfig(TrustAnyServer))
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






