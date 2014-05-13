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
}

sealed trait Method {def name: String}