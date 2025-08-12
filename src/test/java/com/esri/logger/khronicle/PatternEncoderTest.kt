// Copyright 2025 Esri
// 
// Licensed under the Apache License Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.esri.logger.khronicle

import com.esri.logger.khronicle.encoder.PatternEncoder
import java.util.TimeZone
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.slf4j.event.Level
import org.slf4j.helpers.BasicMarkerFactory

class PatternEncoderTest {
@Before
  fun setUp() {
      TimeZone.setDefault(TimeZone.getTimeZone("America/Los_Angeles"))
  }

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

  @Test
  fun encoder_encodesMarkers() {
    val marker = BasicMarkerFactory().getMarker("test")
    val encoder = PatternEncoder("%marker %message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello!"
          this.markers = mutableListOf(marker)
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals("The marker should be encoded as [test]", "[test] Hello!", encodedString)
  }

  @Test
  fun encoder_appendsThrowable_evenWithoutASubstitutionPattern() {
    val encoder = PatternEncoder("%message") // no {} in our string
    val throwable = Exception("test")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello!"
          this.throwable = throwable
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The throwable should be auto appended at the end of the message",
        "Hello!\n${throwable}",
        encodedString)
  }

  @Test
  fun encoder_encodesDate() {
    val currentTimestamp = 1728676162123
    val encoder = PatternEncoder("%date %message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello!"
          this.timeStamp = currentTimestamp
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The timestamp should be encoded as [10-11-2024 12:49:22,123]",
        "10-11-2024 12:49:22,123 Hello!",
        encodedString)
  }

  @Test
  fun encoder_encodesTimestamp() {
    val currentTimestamp = 1234567890L
    val encoder = PatternEncoder("%timestamp %message")
    val loggingEvent =
        LoggingEvent().apply {
          this.level = Level.DEBUG
          this.message = "Hello!"
          this.timeStamp = currentTimestamp
        }

    val encodedString = encoder.encode(loggingEvent).decodeToString()

    assertEquals(
        "The timestamp should be encoded as 1234567890", "1234567890 Hello!", encodedString)
  }
}
