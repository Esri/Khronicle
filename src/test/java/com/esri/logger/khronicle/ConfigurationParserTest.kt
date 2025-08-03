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

import com.esri.logger.khronicle.android.AndroidAPIProvider
import com.esri.logger.khronicle.appender.RollingFileAppender
import com.esri.logger.khronicle.appender.TestAppender
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

    val root = configParser.loggers["root"]
    assertEquals(
        "The logging level should default to ${LoggerConfig.DEFAULT_LEVEL}",
        LoggerConfig.DEFAULT_LEVEL,
        root?.level)
    assertEquals("No appender should be added", 0, root?.appenders?.size)
  }

  @Test
  fun parse_defaultRootLogLevelIsCorrect() {
    val testConfig = "<configuration><root/></configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)

    val root = configParser.loggers["root"]
    assertEquals(
        "The logging level should default to ${LoggerConfig.DEFAULT_LEVEL}",
        LoggerConfig.DEFAULT_LEVEL,
        root?.level)
  }

  @Test
  fun parse_extractsRootLogLevelCorrectly() {
    val testConfig = "<configuration><root level=\"TRACE\" /></configuration>".byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)
    val root = configParser.loggers["root"]
    assertEquals("The logging level should be set to TRACE", Level.TRACE, root?.level)
  }

  @Test
  fun parse_testAppenderGetsAddedWhenPresent() {
    val expectedPattern = "%level %logger %message"
    val testConfig =
        """
            <configuration>
                <appender name="TestAppender" class="com.esri.logger.khronicle.appender.TestAppender">
                    <encoder>
                        <pattern>${expectedPattern}</pattern>
                    </encoder>
                </appender>
                <root>
                    <appender-ref ref="TestAppender" />
                </root>
            </configuration>"
        """
            .trimIndent()
            .byteInputStream()
    val configParser = ConfigurationParser()

    configParser.parse(testConfig)
    val root = configParser.loggers["root"]
    assertEquals("One appender should be added", 1, root?.appenders?.size)
    assertThat("A PrintlnAppender should be added", root?.appenders?.first() is TestAppender)
  }

  @Test
  fun parse_multipleAppendersAreSupported() {
    val testConfig =
        """
          <configuration>
              <appender name="TestAppender" class="com.esri.logger.khronicle.appender.TestAppender"/>
              <root>
                  <appender-ref ref="TestAppender" />
                  <appender-ref ref="TestAppender" />
              </root>
          </configuration>"
      """
            .trimIndent()
            .byteInputStream()

    val configParser = ConfigurationParser()
    configParser.parse(testConfig)
    val root = configParser.loggers["root"]
    assertEquals("Two appenders should be added", 2, root?.appenders?.size)
    assertThat("A TestAppender should be added", root?.appenders?.first() is TestAppender)
    assertThat("A Second TestAppender should be added", root?.appenders?.get(1) is TestAppender)
  }

  @Test
  fun parse_multipleLoggersAreSupported() {
    val testConfig =
        """
          <configuration>
              <appender name="TestAppender" class="com.esri.logger.khronicle.appender.TestAppender"/>
              <logger name="my.test.logger">
                    <appender-ref ref="TestAppender" />
              </logger>
              <root>
                  <appender-ref ref="TestAppender" />
              </root>
          </configuration>"
      """
            .trimIndent()
            .byteInputStream()

    val configParser = ConfigurationParser()
    configParser.parse(testConfig)
    val root = configParser.loggers["root"]
    val testLogger = configParser.loggers["my.test.logger"]
    assertEquals("Two loggers should be added", 2, configParser.loggers.size)
    assertThat(
        "A TestAppender should be added to the root", root?.appenders?.first() is TestAppender)
    assertThat(
        "A TestAppender should be added to \"my.test.logger\"",
        testLogger?.appenders?.first() is TestAppender)
  }

  @Test
  fun parse_rollingFileAppenderGetsAddedCorrectly() {
    AndroidAPIProvider.AppContext = WeakReference(RuntimeEnvironment.getApplication())

    val testConfig =
        """
            <configuration>
                <appender name="file" class="com.esri.logger.khronicle.appender.RollingFileAppender">
                    <file>sample</file>
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
    val root = configParser.loggers["root"]
    val appender = root?.appenders?.first()
    assertEquals("One appender should be added", 1, root?.appenders?.size)
    assertThat("A RollingFileAppender should be added", appender is RollingFileAppender)
    assertThat(
        "File appender should have filename set",
        (appender as RollingFileAppender).file == "sample")
  }
}
