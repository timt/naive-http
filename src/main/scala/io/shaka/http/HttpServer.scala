package io.shaka.http

import java.io.FileInputStream
import java.net.InetSocketAddress
import java.security.{SecureRandom, KeyStore}
import javax.net.ssl._

import com.sun.net.httpserver.spi.HttpServerProvider
import com.sun.net.httpserver.{HttpServer => SunHttpServer, HttpsParameters, HttpsConfigurator, HttpsServer}
import io.shaka.http.Http.HttpHandler
import io.shaka.http.HttpServer.ToLog
import io.shaka.http.Response.respond
import io.shaka.http.Status.NOT_FOUND

class HttpServer(private val usePort: Int = 0, otherLog: ToLog, sslConfig: HttpServerSslConfig = NoSslConfig) {
  import HttpServer.createServer

  val server = createServer(usePort, sslConfig)
  server.setExecutor(null)
  server.createContext("/", new SunHttpHandlerAdapter((req) => respond("No handler defined!").status(NOT_FOUND)))

  def start() = {
    val startedAt = System.nanoTime()
    server.start()
    val elapsedTime = BigDecimal((System.nanoTime() - startedAt) / 1000000.0).formatted("%.2f")
    otherLog(s"naive-http-server started on port $port in $elapsedTime milli seconds")
    this
  }

  def stop() {
    val delayInSeconds = 0
    server.stop(delayInSeconds)
  }

  def port = server.getAddress.getPort

  def handler(handler: HttpHandler) = {
    server.removeContext("/")
    server.createContext("/", new SunHttpHandlerAdapter(handler))
    this
  }
}

object HttpServer {
  type ToLog = String => Unit
  private val printlnLog: ToLog = (s) => println(s)

  def apply(): HttpServer = apply(0)
  def apply(port: Int): HttpServer = new HttpServer(port, printlnLog)
  def apply(handler: HttpHandler, port: Int = 0, log: ToLog = printlnLog): HttpServer = new HttpServer(port, log).handler(handler)

  def https(keyStoreConfig: PathAndPassword, maybeTrustStoreConfig: Option[PathAndPassword] = None, port: Int = 0) =
    new HttpServer(port, printlnLog, SslConfig(keyStoreConfig, maybeTrustStoreConfig))
  def httpsMutualAuth(keyStoreConfig: PathAndPassword, trustStoreConfig: PathAndPassword, port: Int = 0) =
    https(keyStoreConfig, Some(trustStoreConfig), port)

  private def createServer(requestedPort: Int, sslConfig: HttpServerSslConfig): SunHttpServer = {
    val address: InetSocketAddress = new InetSocketAddress(requestedPort)
    val httpServerProvider: HttpServerProvider = HttpServerProvider.provider()

    sslConfig match {
      case NoSslConfig => httpServerProvider.createHttpServer(address, 0)
      case SslConfig(ksConfig, maybeTsConfig) =>

        val ks: KeyStore = KeyStore.getInstance("JKS")
        ks.load(new FileInputStream(ksConfig.path), ksConfig.password.toCharArray)
        val kmf = KeyManagerFactory.getInstance("SunX509")
        kmf.init(ks, ksConfig.password.toCharArray)

        val trustManagers = maybeTsConfig.fold[Array[TrustManager]](null){ tsConfig =>
          val ts: KeyStore = KeyStore.getInstance("JKS")
          ts.load(new FileInputStream(tsConfig.path), tsConfig.password.toCharArray)
          val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
          tmf.init(ts)

          tmf.getTrustManagers
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(kmf.getKeyManagers, trustManagers, new SecureRandom())

        val server: HttpsServer = httpServerProvider.createHttpsServer(address, 0)

        server.setHttpsConfigurator(new HttpsConfigurator(sslContext){
          override def configure(httpsParameters: HttpsParameters) = {
            val sslParameters: SSLParameters = getSSLContext.getDefaultSSLParameters
            sslParameters.setNeedClientAuth(maybeTsConfig.isDefined)
            httpsParameters.setSSLParameters(sslParameters)
          }
        })
        server
    }
  }
}

case class PathAndPassword(path: String, password: String)

sealed trait HttpServerSslConfig
case object NoSslConfig extends HttpServerSslConfig
case class SslConfig(keyStoreConfig: PathAndPassword, trustStoreConfig: Option[PathAndPassword]) extends HttpServerSslConfig
