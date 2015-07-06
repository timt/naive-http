package io.shaka.http

import io.shaka.http.Https.{UseKeyStore, TrustServersByTrustStore}
import io.shaka.http.Request.GET

// TODO Convert into automated test once code dependency has been sorted out
object ManualTestingClientTool extends App {
  val response = Http.http(GET("https://127.0.0.1:7878/foo"))(httpsConfig = Some(io.shaka.http.Https.HttpsConfig(
    TrustServersByTrustStore("../naive-http-server/client-truststore.jks", "password"),
    UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password"))
  ))
  println(response)
}

// TODO Convert into automated test once code dependency has been sorted out
object ManualTestingServerTool extends App {
  private val ksConfig = PathAndPassword("naive-http/src/test/resources/certs/keystore-testing.jks", "password")
  private val tsConfig = PathAndPassword("../naive-http/server-truststore.jks", "password")

  val server = HttpServer.https(io.shaka.http.HttpsConfig(ksConfig, tsConfig), 7878).start()
  println("Mutual SSL auth http server started at https://localhost:" + server.port)
}