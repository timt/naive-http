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

  def `serialize url encodes name and value`(){
    assert(serialize(FormParameter("a question", "what is $20/5")) === "a+question=what+is+%2420%2F5" )
  }

  def `can deserialize a FormParameter`(){
    assert(deserialize("name=value") === FormParameter("name", "value"))
  }

  def `can deserialize a FormParameter with no value`(){
    assert(deserialize("name") === FormParameter("name"))
  }

  def `deserialize url decodes name and value`(){
    assert(deserialize("a+question=what+is+%2420%2F5") === FormParameter("a question", "what is $20/5"))
  }

}
