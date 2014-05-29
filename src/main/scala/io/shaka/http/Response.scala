package io.shaka.http

import io.shaka.http.Status.OK

case class Response(status: Status = OK, headers: Headers = Headers.Empty, entity: Option[Entity] = None)
