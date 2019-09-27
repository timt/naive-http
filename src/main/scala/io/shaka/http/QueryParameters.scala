package io.shaka.http

import io.shaka.http.RequestMatching.URLMatcher
import java.net.URLDecoder.decode
import scala.collection.{mutable ⇒ M}


/**
  * Extract query parameters out of a url & return as a Map[String, List[String]].
  *
  * Use this variant if you expect or care about repetition of keys, i.e. this will happen: key=value&key=value2
  * This is dealt with by a slightly more complex return type: Map[String, List[String]] than the lossy variant.
  */
object QueryParameters {
  def unapply(queryParams: String): Option[QueryParameters] = {
    val mutableResult: M.Map[String, M.ListBuffer[String]] =
      M.Map.empty[String, M.ListBuffer[String]].withDefault(_ => M.ListBuffer[String]())

    queryParams.split("&").map(decode(_, "UTF-8")).foreach {
      case url"$key=$value" => mutableResult += ((key, mutableResult(key) += value))
      case _                =>
    }
    
    val queryParamsMultiMap: Map[String, List[String]] = mutableResult.iterator.map { case (k, v) => (k, v.toList) }.toMap

    Some(QueryParameters(queryParamsMultiMap))
  }
}

case class QueryParameters(values: Map[String, List[String]]){
  def get(key: String): Option[String] = values.get(key).flatMap(_.headOption)
}