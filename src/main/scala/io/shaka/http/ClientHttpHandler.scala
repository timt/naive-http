package io.shaka.http

import io.shaka.http.Http._
import java.net.{HttpURLConnection, URL}
import io.shaka.http.Status._
import scala.Some
import Headers.toHeaders
import IO.inputStreamToByteArray
import proxy.{Proxy, noProxy}

class ClientHttpHandler(proxy: Proxy = noProxy) extends HttpHandler {

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
    val timeoutInMillis = 0
    connection.setUseCaches(false)
    connection.setConnectTimeout(timeoutInMillis)
    connection.setReadTimeout(timeoutInMillis)
    connection.setInstanceFollowRedirects(false)
    connection
  }
}
