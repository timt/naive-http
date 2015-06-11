package io.shaka.http

import io.shaka.http.Https._
import io.shaka.http.Request.GET

// TODO Convert into automated test once code dependency has been sorted out
object ManualTestingTool extends App {
  val response = Http.http(GET("https://127.0.0.1:7878/foo"))(httpsConfig = Some(HttpsConfig(
    TrustServersByTrustStore("../naive-http-server/client-truststore.jks", "password"),
    UseKeyStore("src/test/resources/certs/keystore-testing-client.jks", "password"))
  ))
  println(response)
}