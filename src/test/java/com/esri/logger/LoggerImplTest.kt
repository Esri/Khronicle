package com.esri.logger

import com.esri.logger.appender.TestAppender
import org.junit.Assert.*
import org.junit.Test
import org.slf4j.event.Level

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

    logger.trace("Hello, {}!", null)
    logger.debug("Hello, {}!", null)
    logger.info("Hello, {}!", null)
    logger.warn("Hello, {}!", null)
    logger.error("Hello, {}!", null)
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
}
