package io.shaka.http

object Method {
  case object OPTIONS extends Method {val name = "OPTIONS"}
  case object GET extends Method {val name = "GET"}
  case object HEAD extends Method {val name = "HEAD"}
  case object POST extends Method {val name = "POST"}
  case object PUT extends Method {val name = "PUT"}
  case object DELETE extends Method {val name = "DELETE"}
  case object TRACE extends Method {val name = "TRACE"}
  case object CONNECT extends Method {val name = "CONNECT"}
  case class unknownMethod(name: String) extends Method

  val values = List(
    OPTIONS,
    GET,
    HEAD,
    POST,
    PUT,
    DELETE,
    TRACE,
    CONNECT
  )

  def method(name: String) = values.find(h => h.name == name).getOrElse(unknownMethod(name))
}

sealed trait Method {def name: String}