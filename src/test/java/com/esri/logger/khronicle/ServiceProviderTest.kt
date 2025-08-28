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
    assert(loggerImpl.appenders.first() is TestAppender)
    val testAppender = loggerImpl.appenders.first() as TestAppender
    lateinit var output: String
    testAppender.output = { event -> output = event.message }

    logger.trace("A trace message")
    assertEquals("The message should be logged as-is", "A trace message", output)

    logger.debug("A debug message")
    assertEquals("The message should be logged as-is", "A debug message", output)

    logger.info("An info message")
    assertEquals("The message should be logged as-is", "An info message", output)

    logger.warn("A warn message")
    assertEquals("The message should be logged as-is", "A warn message", output)

    logger.error("An error message")
    assertEquals("The message should be logged as-is", "An error message", output)
  }
}
