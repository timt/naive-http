package io.shaka.http


/*
Handling form parameters
 - Need to be able serialize/deserialze pairs of form parameters (may be just pair._1 with no value) to an from String serialization format stored in the entity
 - Then can simple deserialze->add pair->reserialize
 - Alternatively could store up the form parameters and serialize to String format at last moment
 - Also need to replace the content type in the header with APPLICATION_FORM_URLENCODED
 - serialization takes the form  name1=value1&name2&name3=value3
 */

import FormParameter.{deserialize, serialize}

object FormParameters {
  type FormParameters = List[FormParameter]
  def toEntity(formParameters: FormParameters):Entity = Entity(formParameters.map(serialize).mkString("&"))
  def fromEntity(entity: Entity):FormParameters = entity.toString.split("&").map(deserialize).toList
}