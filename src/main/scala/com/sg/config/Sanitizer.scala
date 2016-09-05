package com.sg.config

class Sanitizer {
  val longRegex = "(\\d+)".r
  val commentRegex = "(.*?)?;(.*?)".r

  def sanitize(string: String): Any = {
    string.toLowerCase match {
      case "yes"| "true" | "\"1\"" | "'1'" | "on" => true
      case "no" | "false" | "\"0\"" | "'0'" | "off" => false
      case longRegex(num) => num.toLong
      case commentRegex(value, comment)  if !string.startsWith("\"") => sanitize(value)
      case x if !x.startsWith("\"") && x.contains(",") => string.split(",")
      case _ => string
    }
  }
}
