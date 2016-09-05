package com.sg.config

import org.scalatest._

class SanitizerSpec extends FlatSpec with Matchers {
  val sanitizer = new Sanitizer()

  "Sanitizer" should "return true if 'yes'" in {
    sanitizer.sanitize("yes") shouldBe true
  }

  "Sanitizer" should "return true if 'true'" in {
    sanitizer.sanitize("true") shouldBe true
  }

  "Sanitizer" should "return true if 1 surrounded with quotes " in {
    sanitizer.sanitize("\"1\"") shouldBe true
  }

  "Sanitizer" should "return true if '1'" in {
    sanitizer.sanitize("'1'") shouldBe true
  }

  "Sanitizer" should "return true if 'on'" in {
    sanitizer.sanitize("on") shouldBe true
  }

  "Sanitizer" should "return false if 'no'" in {
    sanitizer.sanitize("no") shouldBe false
  }

  "Sanitizer" should "return false if 'false'" in {
    sanitizer.sanitize("false") shouldBe false
  }

  "Sanitizer" should "return false if '0'" in {
    sanitizer.sanitize("\"0\"") shouldBe false
  }

  "Sanitizer" should "return false if 'off'" in {
    sanitizer.sanitize("off") shouldBe false
  }

  "Sanitizer" should "not be case sensitive" in {
    sanitizer.sanitize("False") shouldBe false
  }

  "Sanitizer" should "return numbers" in {
    sanitizer.sanitize("1234") shouldBe 1234
  }

  "Sanitizer" should "return array" in {
    sanitizer.sanitize("array,of,values") shouldBe List("array", "of", "values")
  }

  "Sanitizer" should "ignore comments" in {
    sanitizer.sanitize("/srv/uploads/; This is another comment") shouldBe "/srv/uploads/"
  }

  "Sanitizer" should "sanitize string not part of the trailing comment" in {
    sanitizer.sanitize("yes; This is another comment") shouldBe true
  }

  "Sanitizer" should "return empty if no value;" in {
    sanitizer.sanitize("; This is another comment") shouldBe ""
  }

  "Sanitizer" should "not handle ';' as comment if it is surrounded by quotes" in {
    sanitizer.sanitize("\"Hello; This is not a comment\"") shouldBe "\"Hello; This is not a comment\""
  }
}
