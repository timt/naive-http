package io.shaka.http

import java.net.{URLDecoder, URLEncoder}
import java.nio.charset.Charset

case class FormParameter(name: String, value: Option[String])

object FormParameter {
  def apply(name: String): FormParameter = FormParameter(name, None)

  def apply(name: String, value: String): FormParameter = FormParameter(name, Some(value))

  def serialize(formParameter: FormParameter): String = encode(formParameter.name) + (formParameter.value match {
    case Some(value) => s"=${encode(value)}"
    case _ => ""
  })

  private def encode(s: String) = URLEncoder.encode(s, Charset.forName("UTF-8").toString)

  def deserialize(serializedFormParameter: String): FormParameter = if (serializedFormParameter.contains("=")) {
    val pair = serializedFormParameter.split("=")
    FormParameter(decode(pair(0)), Some(decode(pair(1))))
  } else {
    FormParameter(decode(serializedFormParameter))
  }

  private def decode(s: String) = URLDecoder.decode(s, Charset.forName("UTF-8").toString)

}
