package io.shaka.http

object Entity {
  def apply(entity: String): Entity = Entity(entity.getBytes)
}

case class Entity(content: Array[Byte]) {
  override def toString: String = new String(content)
  override def equals(other: scala.Any): Boolean = Option(other).exists(o => o.isInstanceOf[Entity] && o.toString.equals(this.toString))
  override def hashCode(): Int = toString.hashCode
}
