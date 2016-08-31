package com.sg.config

import org.scalatest._

import scala.io.Source

class ConfigSpec extends FlatSpec with Matchers {

  "Config" should "return an existing setting" in {
    val configMap = Map("ftp" -> Map("name"-> "blah"))

    val config = new Config(configMap)

    config.get("ftp")("name") shouldBe "blah"
  }

  "Config" should "handle unknown setting" in {
    val configMap = Map("ftp" -> Map("path<production>" -> "/production"))

    val config = new Config(configMap)

    config.get("ftp")("hello") shouldBe None
  }

}
