package io.shaka.http

import org.scalatest.FunSuite
import FormParameters.{fromEntity, toEntity}

class FormParametersSpec extends FunSuite {

  test("can create FormParameters from Entity") {
    assert(fromEntity(Entity("name1=value1&name2=value2")) === List(FormParameter("name1", "value1"), FormParameter("name2", "value2")))
  }

  test("can create Entity from FormParameters") {
    assert(toEntity(List(FormParameter("name1", "value1"), FormParameter("name2", "value2"))).toString === Entity("name1=value1&name2=value2").toString)
  }

}