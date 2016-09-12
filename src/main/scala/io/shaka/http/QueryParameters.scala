package io.shaka.http

import io.shaka.http.RequestMatching.URLMatcher
import java.net.URLDecoder.decode
import scala.collection.{mutable ⇒ M, breakOut} // http://stackoverflow.com/questions/1715681/scala-2-8-breakout/1716558#1716558


/**
  * Extract query parameters out of a url & return as a Map[String, List[String]].
  *
  * Use this variant if you expect or care about repetition of keys, i.e. this will happen: key=value&key=value2
  * This is dealt with by a slightly more complex return type: Map[String, List[String]] than the lossy variant.
  */
object QueryParameters {
  def unapply(queryParams: String): Option[Map[String, List[String]]] = {
    val mutableResult: M.Map[String, M.ListBuffer[String]] =
      M.Map.empty[String, M.ListBuffer[String]].withDefault(_ => M.ListBuffer[String]())

    decode(queryParams, "UTF-8").split("&").foreach {
      case url"$key=$value" => mutableResult += ((key, mutableResult(key) += value))
      case _                =>
    }

    val queryParamsMultiMap: Map[String, List[String]] = mutableResult.map {
      case (key, valuesBuffer) ⇒ (key, valuesBuffer.toList)
    }(breakOut)

    Some(queryParamsMultiMap)
  }
}