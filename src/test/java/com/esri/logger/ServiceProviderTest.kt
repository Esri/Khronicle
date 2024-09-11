package com.esri.logger

import com.esri.logger.appender.TestAppender
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.robolectric.RobolectricTestRunner
import org.slf4j.LoggerFactory

@RunWith(RobolectricTestRunner::class)
class ServiceProviderTest {
    @Test
    fun loggerFactory_getsAValidTestLogger() {
        // This tests that the Service Provider Interface works:
        // 1. org.slf4j.LoggerFactory.getLogger gets our ServiceProvider
        // 2. ServiceProvider finds the test logger-config.xml configuration file
        // 3. ConfigurationParser parses it correctly
        // 4. ServiceProvider prepares com.esri.logger.LoggerFactory properly
        // We test this by getting a logger through the Service Provider mechanism and get a
        // functioning TestAppender as a result.

        val logger = LoggerFactory.getLogger("testLogger")
        val loggerImpl = logger as LoggerImpl
        assert(loggerImpl.appenders.first is TestAppender)
        val testAppender = loggerImpl.appenders.first as TestAppender
        lateinit var output: String
        testAppender.output = { event -> output = event.message }

        logger.trace("A trace message")
        assertEquals("A trace message", output)

        logger.debug("A debug message")
        assertEquals("A debug message", output)

        logger.info("An info message")
        assertEquals("An info message", output)

        logger.warn("A warn message")
        assertEquals("A warn message", output)

        logger.error("An error message")
        assertEquals("An error message", output)
    }
}