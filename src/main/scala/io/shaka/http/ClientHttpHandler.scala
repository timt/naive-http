package io.shaka.http

import java.net.{HttpURLConnection, URL}
import javax.net.ssl.HttpsURLConnection

import io.shaka.http.Headers.toHeaders
import io.shaka.http.Http._
import io.shaka.http.Https.{sslFactory, hostNameVerifier, HttpsConfig}
import io.shaka.http.IO.inputStreamToByteArray
import io.shaka.http.Status._
import io.shaka.http.proxy.{Proxy, noProxy}

class ClientHttpHandler(proxy: Proxy = noProxy, httpsConfig: Option[HttpsConfig] = None, timeout: Timeout = tenSecondTimeout) extends HttpHandler {

  override def apply(request: Request): Response = {
    val connection = createConnection(request.url, proxy)
    connection.setRequestMethod(request.method.name)
    request.headers.foreach {
      header =>
        connection.setRequestProperty(header._1.name, header._2)
    }
    request.entity.map {
      entity =>
        connection.setDoOutput(true)
        connection.getOutputStream.write(entity.content)
    }
    val response = buildResponse(connection)
    connection.disconnect()
    response
  }

  def buildResponse(connection: HttpURLConnection) = {
    val s = status(connection.getResponseCode, connection.getResponseMessage)
    val headers: Headers = toHeaders(connection.getHeaderFields)
    val entity: Option[Entity] = for {
      is <- Option(if(s.code >=400) connection.getErrorStream else connection.getInputStream)
      content <- Zero.toOption(inputStreamToByteArray(is))
      entity <- Some(Entity(content))
    } yield entity

    Response(
      status = s,
      headers = headers,
      entity = entity
    )
  }

  protected def createConnection(url: Url, proxy: Proxy) = {
    val connection = new URL(url).openConnection(proxy()).asInstanceOf[HttpURLConnection]
    (connection, httpsConfig) match {
      case (c: HttpsURLConnection, Some(config)) =>
        c.setSSLSocketFactory(sslFactory(config))
        c.setHostnameVerifier(hostNameVerifier(config.trustStoreConfig))
      case _ =>
    }
    connection.setUseCaches(false)
    connection.setConnectTimeout(timeout.millis)
    connection.setReadTimeout(timeout.millis)
    connection.setInstanceFollowRedirects(false)
    connection
  }
}

trait SslConnection {
  ClientHttpHandler => {

  }

}

