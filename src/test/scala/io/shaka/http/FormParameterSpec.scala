package io.shaka.http

import org.scalatest.FunSuite
import FormParameter.{serialize, deserialize}

class FormParameterSpec extends FunSuite {

  test("can serialize a FormParameter") {
    assert(serialize(FormParameter("name", "value")) === "name=value" )
  }

  test("can serialize a FormParameter with no value") {
    assert(serialize(FormParameter("name")) === "name" )
  }

  test("serialize url encodes name and value") {
    assert(serialize(FormParameter("a question", "what is $20/5")) === "a+question=what+is+%2420%2F5" )
  }

  test("can deserialize a FormParameter") {
    assert(deserialize("name=value") === FormParameter("name", "value"))
  }

  test("can deserialize a FormParameter with no value") {
    assert(deserialize("name") === FormParameter("name"))
  }

  test("deserialize url decodes name and value") {
    assert(deserialize("a+question=what+is+%2420%2F5") === FormParameter("a question", "what is $20/5"))
  }

}