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
        appender.output = { event -> assertEquals(Level.TRACE, event.level) }

        logger.appenders.add(appender)
        logger.trace(testMessage)
    }

    @Test
    fun debug_containsCorrectLevel() {
        val logger = LoggerImpl("testLogger")
        val testMessage = "Hello, test"
        val appender = TestAppender()
        appender.output = { event -> assertEquals(Level.DEBUG, event.level) }

        logger.appenders.add(appender)
        logger.debug(testMessage)
    }

    @Test
    fun info_containsCorrectLevel() {
        val logger = LoggerImpl("testLogger")
        val testMessage = "Hello, test"
        val appender = TestAppender()
        appender.output = { event -> assertEquals(Level.INFO, event.level) }

        logger.appenders.add(appender)
        logger.info(testMessage)
    }

    @Test
    fun warn_containsCorrectLevel() {
        val logger = LoggerImpl("testLogger")
        val testMessage = "Hello, test"
        val appender = TestAppender()
        appender.output = { event -> assertEquals(Level.WARN, event.level) }

        logger.appenders.add(appender)
        logger.warn(testMessage)
    }

    @Test
    fun error_containsCorrectLevel() {
        val logger = LoggerImpl("testLogger")
        val testMessage = "Hello, test"
        val appender = TestAppender()
        appender.output = { event -> assertEquals(Level.ERROR, event.level) }

        logger.appenders.add(appender)
        logger.error(testMessage)
    }

    @Test
    fun logger_handlesOneArgument() {
        val logger = LoggerImpl("testLogger")
        val appender = TestAppender()
        appender.output = { event ->
            assertEquals(1, event.arguments.size)
            assertEquals("argument", event.arguments.first())
        }
        logger.appenders.add(appender)

        logger.error("Hello, {}!", "argument")
    }

    @Test
    fun logger_handlesTwoArguments() {
        val logger = LoggerImpl("testLogger")
        val appender = TestAppender()
        appender.output = { event ->
            assertEquals(2, event.arguments.size)
            assertEquals("arg1", event.arguments.first())
            assertEquals("arg2", event.arguments[1])
        }
        logger.appenders.add(appender)

        logger.error("Hello, {}-{}!", "arg1", "arg2")
    }

    @Test
    fun logger_handlesMoreArguments() {
        val logger = LoggerImpl("testLogger")
        val appender = TestAppender()
        appender.output = { event ->
            assertEquals(3, event.arguments.size)
            assertEquals("arg1", event.arguments.first())
            assertEquals("arg2", event.arguments[1])
            assertEquals("arg3", event.arguments[2])
        }
        logger.appenders.add(appender)

        logger.error("Hello, {}-{}-{}!", "arg1", "arg2", "arg3")
    }

    @Test
    fun logger_handlesObjectArguments() {
        val logger = LoggerImpl("testLogger")
        val appender = TestAppender()
        appender.output = { event ->
            assertEquals(1, event.arguments.size)
            assertEquals(appender, event.arguments.first())
        }
        logger.appenders.add(appender)

        logger.error("Hello, {}!", appender)
    }

    @Test
    fun logger_handlesOneNullArgument() {
        val logger = LoggerImpl("testLogger")
        val appender = TestAppender()
        appender.output = { event ->
            assertEquals(1, event.arguments.size)
            assertNull(event.arguments[0])
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
            assertEquals(2, event.arguments.size)
            assertNull(event.arguments[0])
            assertNull(event.arguments[1])
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
            assertEquals(3, event.arguments.size)
            assertNull(event.arguments[0])
            assertNull(event.arguments[1])
            assertNull(event.arguments[2])
        }
        logger.appenders.add(appender)

        logger.trace("Hello, {}-{}-{}!", null, null, null)
        logger.debug("Hello, {}-{}-{}!", null, null, null)
        logger.info("Hello, {}-{}-{}!", null, null, null)
        logger.warn("Hello, {}-{}-{}!", null, null, null)
        logger.error("Hello, {}-{}-{}!", null, null, null)
    }
}