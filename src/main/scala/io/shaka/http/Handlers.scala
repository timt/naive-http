package io.shaka.http

import io.shaka.http.Http.HttpHandler
import io.shaka.http.HttpHeader.CONTENT_LENGTH
import io.shaka.http.Method.{GET, HEAD}
import io.shaka.http.Status.INTERNAL_SERVER_ERROR

object Handlers {

  object HEADRequestHandler {
    def ~>(handler: HttpHandler): HttpHandler = (request) => {
      def foldHeadRequest[T](original: T)(doWhenHead: T => T): T = {
        if(request.method == HEAD) doWhenHead(original) else original
      }
      val response = handler(foldHeadRequest(request)(_.copy(method = GET)))
      foldHeadRequest(response)(_.header(CONTENT_LENGTH, response.entity.fold("0")(_.content.length.toString)))


    }
  }

  object SafeRequestHandler {
    def ~>(handler: HttpHandler): HttpHandler = (request) => try {
      handler(request)
    } catch {
      case e: Throwable => Response().entity(s"Server error: ${e.getMessage}").status(INTERNAL_SERVER_ERROR)
    }
  }

}
