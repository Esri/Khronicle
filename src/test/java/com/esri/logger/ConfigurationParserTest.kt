package com.esri.logger

import com.esri.logger.appender.FileAppender
import com.esri.logger.appender.PrintlnAppender

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.robolectric.RobolectricTestRunner
import org.slf4j.event.Level
import org.xmlpull.v1.XmlPullParserException

@RunWith(RobolectricTestRunner::class)
class ConfigurationParserTest {
    @Test
    fun parse_throwsIfInvalidRoot() {
        val invalidConfig = "<feed>".byteInputStream()
        val configParser = ConfigurationParser()

        assertThrows(XmlPullParserException::class.java) {
            configParser.parse(invalidConfig)
        }
    }

    @Test(timeout = 1000)
    fun parse_incompleteRootThrows() {
        val minimalConfig = "<configuration>".byteInputStream()
        val configParser = ConfigurationParser()

        assertThrows(RuntimeException::class.java) {
            configParser.parse(minimalConfig)
        }
    }

    @Test
    fun parse_incompleteInnerElementThrows() {
        val invalidConfig = "<configuration><appender name=\"arbitrary_name\"></configuration>"
            .byteInputStream()
        val configParser = ConfigurationParser()

        assertThrows(XmlPullParserException::class.java) {
            configParser.parse(invalidConfig)
        }
    }

    @Test
    fun parse_minimalConfigCreatesDefaultRootLogger() {
        val minimalConfig = "<configuration />".byteInputStream()
        val configParser = ConfigurationParser()

        configParser.parse(minimalConfig)

        assertEquals(Root.DEFAULT_LEVEL, configParser.root.level)
        assertEquals(0, configParser.root.appenders.size)
    }

    @Test
    fun parse_defaultRootLogLevelIsCorrect() {
        val testConfig = "<configuration><root/></configuration>".byteInputStream()
        val configParser = ConfigurationParser()

        configParser.parse(testConfig)

        assertEquals(Root.DEFAULT_LEVEL, configParser.root.level)
    }

    @Test
    fun parse_extractsRootLogLevelCorrectly() {
        val testConfig = "<configuration><root level=\"TRACE\" /></configuration>".byteInputStream()
        val configParser = ConfigurationParser()

        configParser.parse(testConfig)

        assertEquals(Level.TRACE, configParser.root.level)
    }

    @Test
    fun parse_printlnAppenderGetsAddedByDefault() {
        val expectedPattern = "%level %logger %message"
        val testConfig = """
            <configuration>
                <appender name="stdout">
                    <encoder>
                        <pattern>${expectedPattern}</pattern>
                    </encoder>
                </appender>
                <root>
                    <appender-ref ref="stdout" />
                </root>
            </configuration>"
        """.trimIndent().byteInputStream()
        val configParser = ConfigurationParser()

        configParser.parse(testConfig)

        assertEquals(1, configParser.root.appenders.size)
        assert(configParser.root.appenders.first is PrintlnAppender)
    }

    @Test
    fun parse_fileAppenderGetsAddedCorrectly() {
        val testConfig = """
            <configuration>
                <appender name="file" class="com.esri.logger.appender.FileAppender">
                    <encoder>
                        <pattern>arbitrary pattern</pattern>
                    </encoder>
                </appender>
                <root>
                    <appender-ref ref="file" />
                </root>
            </configuration>"
        """.trimIndent().byteInputStream()
        val configParser = ConfigurationParser()

        configParser.parse(testConfig)

        assertEquals(1, configParser.root.appenders.size)
        assert(configParser.root.appenders.first is FileAppender)
    }
}