package io.shaka.http

import FormParameter.{deserialize, serialize}

object FormParameters {
  type FormParameters = List[FormParameter]
  def toEntity(formParameters: FormParameters):Entity = Entity(formParameters.map(serialize).mkString("&"))
  def fromEntity(entity: Entity):FormParameters = entity.toString.split("&").map(deserialize).toList
}