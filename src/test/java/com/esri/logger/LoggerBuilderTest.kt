package com.esri.logger

import com.esri.logger.appender.TestAppender
import org.junit.Assert.*
import org.junit.Test
import org.slf4j.event.Level

class LoggerBuilderTest {
  @Test
  fun builder_setsLoggerNameCorrectly() {
    val expectedName = "testLogger"
    val logger = LoggerBuilder(expectedName).build()
    assertEquals(expectedName, logger.name)
  }

  @Test
  fun build_canSetTwoAppenders() {
    lateinit var output1: String
    lateinit var output2: String
    val appender1 = TestAppender()
    appender1.output = { event -> output1 = event.message }
    val appender2 = TestAppender()
    appender2.output = { event -> output2 = event.message }
    val testMessage = "test message"
    val logger = LoggerBuilder("testLogger").addAppender(appender1).addAppender(appender2).build()

    logger.info(testMessage)

    assert(output1.contains(testMessage))
    assert(output2.contains(testMessage))
  }

  @Test
  fun build_correctlyPrintsTraceLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.TRACE).addAppender(appender).build()

    logger.trace("hello trace")
    assert(output!!.contains("hello trace"))

    logger.debug("hello debug")
    assert(output!!.contains("hello debug"))

    logger.info("hello info")
    assert(output!!.contains("hello info"))

    logger.warn("hello warn")
    assert(output!!.contains("hello warn"))

    logger.error("hello error")
    assert(output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsDebugLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.DEBUG).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull(output)

    logger.debug("hello debug")
    assert(output!!.contains("hello debug"))

    logger.info("hello info")
    assert(output!!.contains("hello info"))

    logger.warn("hello warn")
    assert(output!!.contains("hello warn"))

    logger.error("hello error")
    assert(output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsInfoLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.INFO).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull(output)

    logger.debug("hello debug")
    assertNull(output)

    logger.info("hello info")
    assert(output!!.contains("hello info"))

    logger.warn("hello warn")
    assert(output!!.contains("hello warn"))

    logger.error("hello error")
    assert(output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsWarnLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.WARN).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull(output)

    logger.debug("hello debug")
    assertNull(output)

    logger.info("hello info")
    assertNull(output)

    logger.warn("hello warn")
    assert(output!!.contains("hello warn"))

    logger.error("hello error")
    assert(output!!.contains("hello error"))
  }

  @Test
  fun build_correctlyPrintsErrorLevel() {
    var output: String? = null
    val appender = TestAppender()
    appender.output = { event -> output = event.message }
    val logger = LoggerBuilder("testLogger").setLogLevel(Level.ERROR).addAppender(appender).build()

    logger.trace("hello trace")
    assertNull(output)

    logger.debug("hello debug")
    assertNull(output)

    logger.info("hello info")
    assertNull(output)

    logger.warn("hello warn")
    assertNull(output)

    logger.error("hello error")
    assert(output!!.contains("hello error"))
  }
}
