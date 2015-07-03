package io.shaka.http

object HttpServerSpecSupport {
  type RootUrl = String
  def withHttpServer(testBlock: (HttpServer, RootUrl) => Unit) {
    val httpServer = HttpServer().start()
    testBlock(httpServer, s"http://localhost:${httpServer.port}")
    httpServer.stop()
  }
}
