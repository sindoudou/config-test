package com.sg.config

class Sanitizer {
  val longRegex = "(\\d+)".r

  def sanitize(string: String): Any = {
    string match {
      case "yes"| "true" => true
      case "no" | "false" => false
      case longRegex(num) => num.toLong
      case x if !x.startsWith("\"") && x.contains(",") => x.split(",")
      case _ => string
    }
  }
}
