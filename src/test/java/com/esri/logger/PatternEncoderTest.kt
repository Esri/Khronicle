package com.esri.logger

import com.esri.logger.encoder.PatternEncoder
import org.junit.Test
import org.junit.Assert.*
import org.slf4j.event.Level

class PatternEncoderTest {
    @Test
    fun encoder_encodesSimpleMessage() {
        val expectedMessage = "Simple message"
        val encoder = PatternEncoder("%message")
        val loggingEvent = LoggingEvent().apply {
            this.level = Level.DEBUG
            this.message = expectedMessage
        }

        val encodedString = encoder.encode(loggingEvent).decodeToString()

        assertEquals(expectedMessage, encodedString)
    }

    @Test
    fun encoder_encodesOneArgument() {
        val encoder = PatternEncoder("%message")
        val loggingEvent = LoggingEvent().apply {
            this.level = Level.DEBUG
            this.message = "Hello, {}!"
            this.arguments = mutableListOf("argument")
        }

        val encodedString = encoder.encode(loggingEvent).decodeToString()

        assertEquals("Hello, argument!", encodedString)
    }

    @Test
    fun encoder_encodesTwoArguments() {
        val encoder = PatternEncoder("%message")
        val loggingEvent = LoggingEvent().apply {
            this.level = Level.DEBUG
            this.message = "Hello, {}-{}!"
            this.arguments = mutableListOf("arg1", "arg2")
        }

        val encodedString = encoder.encode(loggingEvent).decodeToString()

        assertEquals("Hello, arg1-arg2!", encodedString)
    }

    @Test
    fun encoder_encodesMoreArguments() {
        val encoder = PatternEncoder("%message")
        val loggingEvent = LoggingEvent().apply {
            this.level = Level.DEBUG
            this.message = "Hello, {}-{}-{}!"
            this.arguments = mutableListOf("arg1", "arg2", "arg3")
        }

        val encodedString = encoder.encode(loggingEvent).decodeToString()

        assertEquals("Hello, arg1-arg2-arg3!", encodedString)
    }

    @Test
    fun encoder_printsNullArguments() {
        val encoder = PatternEncoder("%message")
        val loggingEvent = LoggingEvent().apply {
            this.level = Level.DEBUG
            this.message = "Hello, {}!"
            this.arguments = mutableListOf(null)
        }

        val encodedString = encoder.encode(loggingEvent).decodeToString()

        assertEquals("Hello, null!", encodedString)
    }
}
