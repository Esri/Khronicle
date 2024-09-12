package com.esri.logger

import com.esri.logger.appender.TestAppender
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.slf4j.event.Level

class LoggerBuilderTest {
  @Test
  fun builder_setsLoggerNameCorrectly() {
    val expectedName = "testLogger"
    val logger = LoggerBuilder(expectedName).build()
    assertEquals("The name of the logger should be $expectedName", expectedName, logger.name)
  }

  @Test
  fun build_canSetTwoAppenders() {
    var output1 = ""
    var output2 = ""
    val appender1 = TestAppender()
    appender1.output = { event -> output1 = event.message }
    val appender2 = TestAppender()
    appender2.output = { event -> output2 = event.message }
    val testMessage = "test message"
    val logger = LoggerBuilder("testLogger").addAppender(appender1).addAppender(appender2).build()

    logger.info(testMessage)

    assertThat("The output should contain the message", output1.contains(testMessage))
    assertThat("The output should contain the message", output2.contains(testMessage))
  }

  @Test
  fun build_correctlyPrintsTraceLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.TRACE).addAppender(appender).build()

    logger.trace("hello trace")
    assertThat("A trace message should be logged at trace level", output!!.contains("hello trace"))

    logger.debug("hello debug")
    assertThat("A debug message should be logged at trace level", output!!.contains("hello debug"))

    logger.info("hello info")
    assertThat("An info message should be logged at trace level", output!!.contains("hello info"))

    logger.warn("hello warn")
    assertThat("A warn message should be logged at trace level", output!!.contains("hello warn"))

    logger.error("hello error")
    assertThat("An error message should be logged at trace level", output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsDebugLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.DEBUG).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull("A trace message should not be logged at debug level", output)

    logger.debug("hello debug")
    assertThat("A debug message should be logged at debug level", output!!.contains("hello debug"))

    logger.info("hello info")
    assertThat("An info message should be logged at debug level", output!!.contains("hello info"))

    logger.warn("hello warn")
    assertThat("A warn message should be logged at debug level", output!!.contains("hello warn"))

    logger.error("hello error")
    assertThat("An error message should be logged at debug level", output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsInfoLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.INFO).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull("A trace message should not be logged at info level", output)

    logger.debug("hello debug")
    assertNull("A debug message should not be logged at info level", output)

    logger.info("hello info")
    assertThat("An info message should be logged at info level", output!!.contains("hello info"))

    logger.warn("hello warn")
    assertThat("A warn message should be logged at info level", output!!.contains("hello warn"))

    logger.error("hello error")
    assertThat("An error message should be logged at info level", output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsWarnLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.WARN).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull("A trace message should not be logged at warn level", output)

    logger.debug("hello debug")
    assertNull("A debug message should not be logged at warn level", output)

    logger.info("hello info")
    assertNull("An info message should not be logged at warn level", output)

    logger.warn("hello warn")
    assertThat("A warn message should be logged at warn level", output!!.contains("hello warn"))

    logger.error("hello error")
    assertThat("An error message should be logged at warn level", output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsErrorLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.ERROR).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull("A trace message should not be logged at error level", output)

    logger.debug("hello debug")
    assertNull("A debug message should not be logged at error level", output)

    logger.info("hello info")
    assertNull("An info message should not be logged at error level", output)

    logger.warn("hello warn")
    assertNull("A warn message should not be logged at error level", output)

    logger.error("hello error")
    assertThat("An error message should be logged at error level", output!!.contains("hello error"))
  }
}
