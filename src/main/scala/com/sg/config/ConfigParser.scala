package com.sg.config

import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable
import scala.io.Source

object ConfigParser extends Sanitizer with LazyLogging {
  val groupRegexp = "\\[(.*?)\\]".r
  val settingRegexp = "(.*?)=(.*?)".r
  val overriddenRegexp = "(.*?)<(.*?)>".r

  def loadConfig(filePath: String, overrides: List[String] = List.empty): Config = {
    new Config(parseConfig(filePath, overrides))
  }

  def parseConfig(filePath: String, overrides: List[String] = List.empty): Map[Group, Map[Setting, Any]] = {
    val configMap = mutable.LinkedHashMap[Group, mutable.Map[Setting, Any]]()

    for (line <- Source.fromFile(filePath).getLines()) {
      line match {
        case groupRegexp(group)  =>
          configMap(group) = mutable.Map[Setting, Any]()
        case settingRegexp(name, value) =>
          name.trim() match {
            case overriddenRegexp(settingName, overrideValue) if !settingName.contains(";") =>
              if (overrides.isEmpty || overrides.contains(overrideValue)) {
                val groupMap = configMap.lastOption.getOrElse(throw new IllegalArgumentException("No group found for setting"))
                groupMap._2 += (settingName.trim -> sanitize(value.trim))
              }
            case settingName if !settingName.contains(";") =>
              val groupMap = configMap.lastOption.getOrElse(throw new IllegalArgumentException("No group found for setting"))
              groupMap._2 += (name.trim -> sanitize(value.trim))
            case _ =>
              logger.warn(s"Ignoring setting with comments for $name")
          }

        case _ => logger.warn(s"Ignoring config for $line")
      }
    }
    // Returning an immutable map
    configMap.map(kv => (kv._1,kv._2.toMap)).toMap
  }

}
