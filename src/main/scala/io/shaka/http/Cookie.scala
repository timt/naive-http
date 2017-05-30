package io.shaka.http

case class Cookie(name: String, value: String){
  lazy val toSetCookie = s"$name=$value"
}