package io.shaka.http

object Entity {
  def apply(entity: String): Entity = Entity(entity.getBytes)
}

case class Entity(content: Array[Byte]) {
  override def toString: String = new String(content)
}
