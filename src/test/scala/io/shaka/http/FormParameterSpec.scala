package io.shaka.http

import org.scalatest.Spec
import FormParameter.{serialize, deserialize}

class FormParameterSpec extends Spec {

  def `can serialize a FormParameter`() {
    assert(serialize(FormParameter("name", "value")) === "name=value" )
  }

  def `can serialize a FormParameter with no value`() {
    assert(serialize(FormParameter("name")) === "name" )
  }

  def `serialize urlencodes name and value`(){
    assert(serialize(FormParameter("a question", "what is $20/5")) === "a+question=what+is+%2420%2F5" )
  }

  //urlencodes name and value when serializing
  //urldecodes name and value when deserializing

}
