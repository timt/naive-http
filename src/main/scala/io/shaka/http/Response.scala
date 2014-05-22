package io.shaka.http

import io.shaka.http.Status.OK
import io.shaka.http.Http._

case class Response(status: Status = OK, headers: Headers = List(), entity: Option[Entity] = None)
