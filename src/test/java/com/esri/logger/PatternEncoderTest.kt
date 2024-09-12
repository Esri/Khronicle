package com.esri.logger

import com.esri.logger.encoder.PatternEncoder
import org.junit.Assert.*
import org.junit.Test
import org.slf4j.event.Level

class PatternEncoderTest {
  @Test
  fun encoder_encodesSimpleMessage() {
    val expectedMessage = "Simple message"
    val encoder = PatternEncoder("%message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = expectedMessage
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The decoded message should be equal to the encoded message",
        expectedMessage,
        encodedString)
  }

  @Test
  fun encoder_encodesOneArgument() {
    val expectedArg = "argument"
    val encoder = PatternEncoder("%message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello, {}!"
          this.arguments = mutableListOf(expectedArg)
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The argument should be encoded as \"$expectedArg\"", "Hello, $expectedArg!", encodedString)
  }

  @Test
  fun encoder_encodesTwoArguments() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val encoder = PatternEncoder("%message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello, {}-{}!"
          this.arguments = mutableListOf(expectedArg1, expectedArg2)
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The arguments should be encoded as \"$expectedArg1\" and \"$expectedArg2\"",
        "Hello, $expectedArg1-$expectedArg2!",
        encodedString)
  }

  @Test
  fun encoder_encodesMoreArguments() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val expectedArg3 = "arg3"
    val encoder = PatternEncoder("%message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello, {}-{}-{}!"
          this.arguments = mutableListOf(expectedArg1, expectedArg2, expectedArg3)
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The arguments should be encoded as \"$expectedArg1\", \"$expectedArg2\" and \"$expectedArg3\"",
        "Hello, $expectedArg1-$expectedArg2-$expectedArg3!",
        encodedString)
  }

  @Test
  fun encoder_printsNullArguments() {
    val encoder = PatternEncoder("%message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello, {}!"
          this.arguments = mutableListOf(null)
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals("The argument should be encoded as \"null\"", "Hello, null!", encodedString)
  }
}
