package io.shaka.http

import io.shaka.http.RequestMatching.URLMatcher
import java.net.URLDecoder.decode
import scala.collection.{mutable ⇒ M, breakOut} // http://stackoverflow.com/questions/1715681/scala-2-8-breakout/1716558#1716558

object QueryParameters {
  def unapply(queryParams: String): Option[QueryParameters] = {
    val mutableResult: M.Map[String, M.ListBuffer[String]] =
      M.Map.empty[String, M.ListBuffer[String]].withDefault(_ => M.ListBuffer[String]())

    val pairs = queryParams.split("&")
    pairs.map(decode(_, "UTF-8")).foreach {
      case url"$key=$value"      => mutableResult += ((key, mutableResult(key) += value))
      case url"$key="            => mutableResult += ((key, mutableResult(key)))
      case key if key.length > 0 => mutableResult += ((key, mutableResult(key)))
      case _                     =>
    }

    val queryParamsMultiMap: Map[String, List[String]] = mutableResult.map {
      case (key, valuesBuffer) ⇒ (key, valuesBuffer.toList)
    }(breakOut)

    Some(QueryParameters(queryParamsMultiMap))
  }
}

case class QueryParameters(values: Map[String, List[String]]){
  def get(key: String): Option[String] = values.get(key).flatMap(_.headOption)
}
