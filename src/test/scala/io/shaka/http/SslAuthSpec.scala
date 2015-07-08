package io.shaka.http

import io.shaka.http.Https.{DoNotUseKeyStore, HttpsConfig, TrustServersByTrustStore, UseKeyStore}
import io.shaka.http.Request.GET
import io.shaka.http.Response.respond
import io.shaka.http.Status.OK
import org.scalatest.{BeforeAndAfterAll, FunSuite}


class SslAuthSpec extends FunSuite with BeforeAndAfterAll {
  var server: HttpServer = _

  test("Client can connect to server on SSL when the client specifies a certificate"){
    val response = Http.http(GET(s"https://127.0.0.1:${server.port}/foo"))(httpsConfig = Some(HttpsConfig(
      TrustServersByTrustStore("client-truststore.jks", "password"),
      UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password"))
    ))

    assert(statusAndBody(response) === (OK, Some("Hello world")))
  }

  test("Client can connect to server on SSL when the client does not specify a certificate"){
    val response: Response = Http.http(GET(s"https://127.0.0.1:${server.port}/foo"))(httpsConfig = Some(HttpsConfig(
      TrustServersByTrustStore("client-truststore.jks", "password"),
      DoNotUseKeyStore
    )))

    assert(statusAndBody(response) === (OK, Some("Hello world")))
  }

  override protected def beforeAll() = {
    server = HttpServer.https(
      keyStoreConfig = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password")
    ).handler(_ => respond("Hello world")).start()
  }

  override protected def afterAll() = {
    server.stop()
  }

  private def statusAndBody(response: Response): (Status, Option[String]) = (response.status, response.entity.map(_.toString))
}