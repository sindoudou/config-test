package com.sg.config

import org.scalatest._

import scala.io.Source

class ConfigParserSpec extends FlatSpec with Matchers {

  "ConfigParser" should "parse file and return a map of config" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile)
    configMap("ftp")("name") shouldBe "\"hello there, ftp uploading\""
  }

  "ConfigParser" should "parse file with override values" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile, List("production", "ubuntu"))

    configMap("ftp") shouldBe Map(
      "name" -> "\"hello there, ftp uploading\"", "path" -> "/etc/var/uploads", "enabled" -> false)
  }

  "ConfigParser" should "parse file and sanitize boolean value" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile)
    configMap("ftp")("enabled") shouldBe false
  }

  "ConfigParser" should "parse file and return int value" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile)
    configMap("common")("paid_users_size_limit") shouldBe 2147483648L
  }

  "ConfigParser" should "parse file and handle arrays" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile)
    configMap("http")("params") shouldBe List("array", "of", "values")
  }

  "ConfigParser" should "throw an error if the config is not well formed" in {
    val filePath = Source.getClass().getResource("/test_config_error.txt")
    an [IllegalArgumentException] should be thrownBy ConfigParser.parseConfig(filePath.getFile)
  }
}
