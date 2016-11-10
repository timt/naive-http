package io.shaka.http

case class RequestAssertions(request: Request) {
  val body: Option[Entity] = request.entity


  import org.scalatest.Matchers._

  def assertHeader(key: HttpHeader, value: String) {
    request.headers.foreach(println)
    assert(request.headers.contains(key, value), s"request did not contain $key header, $value")
  }

  def assertEntity(entity: String) {
    assert(body.contains(Entity(entity)))
  }
}
