package io.shaka.http

import io.shaka.http.Http._
import java.net.{InetSocketAddress, HttpURLConnection, URL}
import io.shaka.http.Status._
import scala.Some
import Headers.toHeaders
import IO.inputStreamToByteArray
import java.net.{Proxy => JavaProxy}
import JavaProxy.Type.HTTP

case class Proxy(host: String, port: Int)

class ClientHttpHandler(proxy: Option[Proxy] = None) extends HttpHandler {

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
    buildResponse(connection)
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

  private def createConnection(url: Url, proxy: Option[Proxy]) = {
    val theUrl = new URL(url)
    val connection = proxy
      .map(p => new JavaProxy(HTTP, new InetSocketAddress(p.host,p.port)))
      .fold(theUrl.openConnection())(theUrl.openConnection).asInstanceOf[HttpURLConnection]
    val timeoutInMillis = 0
    connection.setUseCaches(false)
    connection.setConnectTimeout(timeoutInMillis)
    connection.setReadTimeout(timeoutInMillis)
    connection.setInstanceFollowRedirects(false)
    connection
  }


}

