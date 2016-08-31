package com.sg.config

object Config {
  def apply(filePath: String, overrides: List[String]= List.empty) =
    ConfigParser.loadConfig(filePath, overrides)
}

class Config(private val configMap: Map[Group, Map[Setting, Any]]) {

  def get(group: Group)(setting: Setting): Any =
    configMap.getOrElse(group, Map.empty).getOrElse(setting, None)

}
