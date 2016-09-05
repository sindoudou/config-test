package com.sg.config

import java.io.FileNotFoundException

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

  "ConfigParser" should "throw an exception if the config file does not exist" in {
    an [FileNotFoundException]  should be thrownBy ConfigParser.parseConfig("/blah")
  }

  "ConfigParser" should "ignore not well formed settings" in {
    val filePath = Source.getClass().getResource("/test_config_with_comment_error.txt")
    val configMap = ConfigParser.parseConfig(filePath.getFile, List("staging"))
    val httpConfig = configMap("http")
    httpConfig.get("test") shouldBe None
    httpConfig.get("test;") shouldBe None
  }

  "ConfigParser" should "not handle ';' as a comment if it is part of a value" in {
    val filePath = Source.getClass().getResource("/test_config")
    val configMap = ConfigParser.parseConfig(filePath.getFile)
    configMap("http")("name2") shouldBe "\"hello there; http uploading\""
  }
}
