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

import com.esri.logger.khronicle.appender.TestAppender
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.slf4j.MarkerFactory
import org.slf4j.event.Level

@RunWith(RobolectricTestRunner::class)
class LoggerImplTest {
  @Test
  fun trace_containsCorrectLevel() {
    val logger = LoggerImpl("testLogger")
    val testMessage = "Hello, test"
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("The logging event should have trace level", Level.TRACE, event.level)
    }

    logger.appenders.add(appender)
    logger.trace(testMessage)
  }

  @Test
  fun debug_containsCorrectLevel() {
    val logger = LoggerImpl("testLogger")
    val testMessage = "Hello, test"
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("The logging event should have debug level", Level.DEBUG, event.level)
    }

    logger.appenders.add(appender)
    logger.debug(testMessage)
  }

  @Test
  fun info_containsCorrectLevel() {
    val logger = LoggerImpl("testLogger")
    val testMessage = "Hello, test"
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("The logging event should have info level", Level.INFO, event.level)
    }

    logger.appenders.add(appender)
    logger.info(testMessage)
  }

  @Test
  fun warn_containsCorrectLevel() {
    val logger = LoggerImpl("testLogger")
    val testMessage = "Hello, test"
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("The logging event should have warn level", Level.WARN, event.level)
    }

    logger.appenders.add(appender)
    logger.warn(testMessage)
  }

  @Test
  fun error_containsCorrectLevel() {
    val logger = LoggerImpl("testLogger")
    val testMessage = "Hello, test"
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("The logging event should have error level", Level.ERROR, event.level)
    }

    logger.appenders.add(appender)
    logger.error(testMessage)
  }

  @Test
  fun logger_handlesOneArgument() {
    val expectedArgument = "argument"
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be one argument", 1, event.arguments.size)
      assertEquals(
          "The argument should be \"$expectedArgument\"", expectedArgument, event.arguments.first())
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}!", expectedArgument)
    logger.debug("Hello, {}!", expectedArgument)
    logger.info("Hello, {}!", expectedArgument)
    logger.warn("Hello, {}!", expectedArgument)
    logger.error("Hello, {}!", expectedArgument)
  }

  @Test
  fun logger_handlesTwoArguments() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be two arguments", 2, event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals(
          "The second argument should be \"$expectedArg2\"", expectedArg2, event.arguments[1])
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.debug("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.info("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.warn("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.error("Hello, {}-{}!", expectedArg1, expectedArg2)
  }

  @Test
  @Suppress("LoggingPlaceholderCountMatchesArgumentCount")
  fun logger_handlesTwoArguments_whereSecondIsAThrowable() {
    val expectedArg1 = "arg1"
    val expectedArg2 = Exception("Test")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals(
          "There should be one argument after throwable is moved to its expected param",
          1,
          event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals("Throwable should be \"$expectedArg2\"", expectedArg2, event.throwable)
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.debug("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.info("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.warn("Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.error("Hello, {}-{}!", expectedArg1, expectedArg2)
  }

  @Test
  fun logger_handlesMoreArguments() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val expectedArg3 = "arg3"
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be three arguments", 3, event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals(
          "The second argument should be \"$expectedArg2\"", expectedArg2, event.arguments[1])
      assertEquals(
          "The third argument should be \"$expectedArg3\"", expectedArg3, event.arguments[2])
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.debug("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.info("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.warn("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.error("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
  }

  @Test
  @Suppress("LoggingPlaceholderCountMatchesArgumentCount")
  fun logger_handlesMoreArguments_whereLastIsAThrowable() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val expectedArg3 = Exception("test")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals(
          "There should be two arguments after throwable is moved to its expected param",
          2,
          event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals(
          "The second argument should be \"$expectedArg2\"", expectedArg2, event.arguments[1])
      assertEquals("Throwable should be \"$expectedArg3\"", expectedArg3, event.throwable)
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.debug("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.info("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.warn("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.error("Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
  }

  @Test
  fun logger_handlesObjectArguments() {
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be one argument", 1, event.arguments.size)
      assertEquals(
          "The appender should be passed as an argument", appender, event.arguments.first())
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}!", appender)
    logger.debug("Hello, {}!", appender)
    logger.info("Hello, {}!", appender)
    logger.warn("Hello, {}!", appender)
    logger.error("Hello, {}!", appender)
  }

  @Test
  fun logger_handlesOneNullArgument() {
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 1 argument", 1, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}!", arg = null)
    logger.debug("Hello, {}!", arg = null)
    logger.info("Hello, {}!", arg = null)
    logger.warn("Hello, {}!", arg = null)
    logger.error("Hello, {}!", arg = null)
  }

  @Test
  fun logger_handlesTwoNullArguments() {
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 2 arguments", 2, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
      assertNull("The passed argument should be null", event.arguments[1])
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}!", null, null)
    logger.debug("Hello, {}-{}!", null, null)
    logger.info("Hello, {}-{}!", null, null)
    logger.warn("Hello, {}-{}!", null, null)
    logger.error("Hello, {}-{}!", null, null)
  }

  @Test
  fun logger_handlesThreeNullArguments() {
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 3 arguments", 3, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
      assertNull("The passed argument should be null", event.arguments[1])
      assertNull("The passed argument should be null", event.arguments[2])
    }
    logger.appenders.add(appender)

    logger.trace("Hello, {}-{}-{}!", null, null, null)
    logger.debug("Hello, {}-{}-{}!", null, null, null)
    logger.info("Hello, {}-{}-{}!", null, null, null)
    logger.warn("Hello, {}-{}-{}!", null, null, null)
    logger.error("Hello, {}-{}-{}!", null, null, null)
  }

  @Test
  fun logger_handlesThrowable() {
    val logger = LoggerImpl("testLogger")
    val message = "Hello!"
    val throwable = Exception("test")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be a throwable", throwable, event.throwable)
      assertEquals("Message should be passed to appender", message, event.message)
    }
    logger.appenders.add(appender)

    logger.trace(message, throwable)
    logger.debug(message, throwable)
    logger.info(message, throwable)
    logger.warn(message, throwable)
    logger.error(message, throwable)
  }

  @Test
  fun logger_handlesOneArgument_andAMarker() {
    val expectedArgument = "argument"
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be one argument", 1, event.arguments.size)
      assertEquals(
          "The argument should be \"$expectedArgument\"", expectedArgument, event.arguments.first())
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}!", expectedArgument)
    logger.debug(marker, "Hello, {}!", expectedArgument)
    logger.info(marker, "Hello, {}!", expectedArgument)
    logger.warn(marker, "Hello, {}!", expectedArgument)
    logger.error(marker, "Hello, {}!", expectedArgument)
  }

  @Test
  fun logger_handlesTwoArguments_andAMarker() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be two arguments", 2, event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals(
          "The second argument should be \"$expectedArg2\"", expectedArg2, event.arguments[1])
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.debug(marker, "Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.info(marker, "Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.warn(marker, "Hello, {}-{}!", expectedArg1, expectedArg2)
    logger.error(marker, "Hello, {}-{}!", expectedArg1, expectedArg2)
  }

  @Test
  fun logger_handlesMoreArguments_andAMarker() {
    val expectedArg1 = "arg1"
    val expectedArg2 = "arg2"
    val expectedArg3 = "arg3"
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be three arguments", 3, event.arguments.size)
      assertEquals(
          "The first argument should be \"$expectedArg1\"", expectedArg1, event.arguments.first())
      assertEquals(
          "The second argument should be \"$expectedArg2\"", expectedArg2, event.arguments[1])
      assertEquals(
          "The third argument should be \"$expectedArg3\"", expectedArg3, event.arguments[2])
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.debug(marker, "Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.info(marker, "Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.warn(marker, "Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
    logger.error(marker, "Hello, {}-{}-{}!", expectedArg1, expectedArg2, expectedArg3)
  }

  @Test
  fun logger_handlesObjectArguments_andAMarker() {
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be one argument", 1, event.arguments.size)
      assertEquals(
          "The appender should be passed as an argument", appender, event.arguments.first())
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}!", appender)
    logger.debug(marker, "Hello, {}!", appender)
    logger.info(marker, "Hello, {}!", appender)
    logger.warn(marker, "Hello, {}!", appender)
    logger.error(marker, "Hello, {}!", appender)
  }

  @Test
  fun logger_handlesOneNullArgument_andAMarker() {
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 1 argument", 1, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}!", arg = null)
    logger.debug(marker, "Hello, {}!", arg = null)
    logger.info(marker, "Hello, {}!", arg = null)
    logger.warn(marker, "Hello, {}!", arg = null)
    logger.error(marker, "Hello, {}!", arg = null)
  }

  @Test
  fun logger_handlesTwoNullArguments_andAMarker() {
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 2 arguments", 2, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
      assertNull("The passed argument should be null", event.arguments[1])
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}-{}!", null, null)
    logger.debug(marker, "Hello, {}-{}!", null, null)
    logger.info(marker, "Hello, {}-{}!", null, null)
    logger.warn(marker, "Hello, {}-{}!", null, null)
    logger.error(marker, "Hello, {}-{}!", null, null)
  }

  @Test
  fun logger_handlesThreeNullArguments_andAMarker() {
    val marker = MarkerFactory.getMarker("marker")
    val logger = LoggerImpl("testLogger")
    val appender = TestAppender()
    appender.output = { event ->
      assertEquals("There should be 3 arguments", 3, event.arguments.size)
      assertNull("The passed argument should be null", event.arguments[0])
      assertNull("The passed argument should be null", event.arguments[1])
      assertNull("The passed argument should be null", event.arguments[2])
      assertTrue("Marker should be passed to the appender", event.markers.contains(marker))
    }
    logger.appenders.add(appender)

    logger.trace(marker, "Hello, {}-{}-{}!", null, null, null)
    logger.debug(marker, "Hello, {}-{}-{}!", null, null, null)
    logger.info(marker, "Hello, {}-{}-{}!", null, null, null)
    logger.warn(marker, "Hello, {}-{}-{}!", null, null, null)
    logger.error(marker, "Hello, {}-{}-{}!", null, null, null)
  }
}
