package io.shaka.http

import org.scalatest.{BeforeAndAfterEach, FunSuite}
import io.shaka.http.Request.GET
import io.shaka.http.Status.OK
import io.shaka.http.Http.http


class HttpsSpec extends FunSuite with BeforeAndAfterEach {
  var server: TestHttpServer = _

  test("does HTTPS"){
    implicit val https = Some(HttpsKeyStore("src/test/certs/keystore-testing.jks", "password"))

    val expected = "helloworld"
    val response = http(GET(server.url + expected))
    assert(response.status === OK)
    assert(response.entityAsString === expected)
  }

  override protected def beforeEach(){server = TestHttpServer.startHttps("src/test/certs/keystore-testing.jks", "password")}
  override protected def afterEach(){server.stop()}
}