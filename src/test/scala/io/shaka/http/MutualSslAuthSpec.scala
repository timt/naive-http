package io.shaka.http

import javax.net.ssl.SSLHandshakeException

import io.shaka.http.Https.{DoNotUseKeyStore, HttpsConfig, TrustServersByTrustStore, UseKeyStore}
import io.shaka.http.Request.GET
import io.shaka.http.Response.respond
import io.shaka.http.Status.OK
import org.scalatest.{BeforeAndAfterAll, FunSuite}


class MutualSslAuthSpec extends FunSuite with BeforeAndAfterAll {
  var server: HttpServer = _

  test("Client can connect to server doing mutual SSL auth") {
    val response = Http.http(GET(s"https://127.0.0.1:${server.port}/foo"))(httpsConfig = Some(HttpsConfig(
      TrustServersByTrustStore("client-truststore.jks", "password"),
      UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password")
    )))

    assert(statusAndBody(response) === (OK, Some("Hello world")))
  }

  test("Client cannot connect to server doing mutual SSL auth without specifying client certificate"){
    intercept[SSLHandshakeException]{
      Http.http(GET(s"https://127.0.0.1:${server.port}/foo"))(httpsConfig = Some(HttpsConfig(
        TrustServersByTrustStore("client-truststore.jks", "password"),
        DoNotUseKeyStore
      )))
    }
  }

  override protected def beforeAll() = {
    server = HttpServer.httpsMutualAuth(
      keyStoreConfig = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password"),
      trustStoreConfig = PathAndPassword("server-truststore.jks", "password")).handler(_ => respond("Hello world")).start()
  }

  override protected def afterAll() = {
    server.stop()
  }

  private def statusAndBody(response: Response): (Status, Option[String]) = (response.status, response.entity.map(_.toString))
}