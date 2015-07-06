package io.shaka.http

import java.net.InetSocketAddress

import com.sun.net.httpserver.{HttpServer => SunHttpServer}
import io.shaka.http.Http.HttpHandler
import io.shaka.http.HttpServer.ToLog
import io.shaka.http.Response.respond
import io.shaka.http.Status.NOT_FOUND

class HttpServer(private val usePort: Int = 0, otherLog: ToLog) {
  import HttpServer.createServer

  val server = createServer(usePort)
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

  private def createServer(requestedPort: Int) = SunHttpServer.create(new InetSocketAddress(requestedPort), 0)
}