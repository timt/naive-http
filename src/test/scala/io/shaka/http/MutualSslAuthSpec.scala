package io.shaka.http

import io.shaka.http.Https.{HttpsConfig, TrustServersByTrustStore, UseKeyStore}
import io.shaka.http.Request.GET
import io.shaka.http.Response.respond
import io.shaka.http.Status.OK
import org.scalatest.{BeforeAndAfterEach, FunSuite}


class MutualSslAuthSpec extends FunSuite with BeforeAndAfterEach {
  test("Client can connect to server doing mutual SSL auth") {
    val server = HttpServer.httpsMutualAuth(
      keyStoreConfig = PathAndPassword("src/test/resources/certs/keystore-testing.jks", "password"),
      trustStoreConfig = PathAndPassword("server-truststore.jks", "password")).handler(_ => respond("Hello world")).start()
    try {
      val response = Http.http(GET(s"https://127.0.0.1:${server.port}/foo"))(httpsConfig = Some(HttpsConfig(
        TrustServersByTrustStore("client-truststore.jks", "password"),
        UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password"))
      ))

      assert((response.status, response.entity.map(_.toString)) === (OK, Some("Hello world")))

    } finally {
      server.stop()
    }
  }
}