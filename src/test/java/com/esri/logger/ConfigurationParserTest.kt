package com.esri.logger

import com.esri.logger.android.AndroidAPIProvider
import com.esri.logger.appender.FileAppender
import com.esri.logger.appender.PrintlnAppender
import java.lang.ref.WeakReference
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.slf4j.event.Level
import org.xmlpull.v1.XmlPullParserException

@RunWith(RobolectricTestRunner::class)
class ConfigurationParserTest {
  @Test
  fun parse_throwsIfInvalidRoot() {
    val invalidConfig = "<feed>".byteInputStream()
    val configParser = ConfigurationParser()

    assertThrows(XmlPullParserException::class.java) { configParser.parse(invalidConfig) }
  }

  @Test(timeout = 1000)
  fun parse_incompleteRootThrows() {
    val minimalConfig = "<configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    assertThrows(RuntimeException::class.java) { configParser.parse(minimalConfig) }
  }

  @Test
  fun parse_incompleteInnerElementThrows() {
    val invalidConfig =
        "<configuration><appender name=\"arbitrary_name\"></configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    assertThrows(XmlPullParserException::class.java) { configParser.parse(invalidConfig) }
  }

  @Test
  fun parse_minimalConfigCreatesDefaultRootLogger() {
    val minimalConfig = "<configuration />".byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(minimalConfig)

    assertEquals(
        "The logging level should default to ${Root.DEFAULT_LEVEL}",
        Root.DEFAULT_LEVEL,
        configParser.root.level)
    assertEquals("No appender should be added", 0, configParser.root.appenders.size)
  }

  @Test
  fun parse_defaultRootLogLevelIsCorrect() {
    val testConfig = "<configuration><root/></configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)

    assertEquals(
        "The logging level should default to ${Root.DEFAULT_LEVEL}",
        Root.DEFAULT_LEVEL,
        configParser.root.level)
  }

  @Test
  fun parse_extractsRootLogLevelCorrectly() {
    val testConfig = "<configuration><root level=\"TRACE\" /></configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)

    assertEquals("The logging level should be set to TRACE", Level.TRACE, configParser.root.level)
  }

  @Test
  fun parse_printlnAppenderGetsAddedByDefault() {
    val expectedPattern = "%level %logger %message"
    val testConfig =
        """
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
        """
            .trimIndent()
            .byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)

    assertEquals("One appender should be added", 1, configParser.root.appenders.size)
    assertThat(
        "A PrintlnAppender should be added", configParser.root.appenders.first is PrintlnAppender)
  }

  @Test
  fun parse_fileAppenderGetsAddedCorrectly() {
    AndroidAPIProvider.AppContext = WeakReference(RuntimeEnvironment.getApplication())

    val testConfig =
        """
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
        """
            .trimIndent()
            .byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)

    assertEquals("One appender should be added", 1, configParser.root.appenders.size)
    assertThat("A FileAppender should be added", configParser.root.appenders.first is FileAppender)
  }
}
