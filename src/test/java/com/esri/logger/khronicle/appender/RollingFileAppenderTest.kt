// Copyright 2026 Esri
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

package com.esri.logger.khronicle.appender

import com.esri.logger.khronicle.android.AndroidAPIProvider
import io.mockk.every
import io.mockk.mockk
import java.io.File
import java.lang.ref.WeakReference
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.slf4j.event.LoggingEvent

@RunWith(RobolectricTestRunner::class)
class RollingFileAppenderTest {

  private lateinit var logsDir: File

  @Before
  fun installContextAndCleanLogsDir() {
    AndroidAPIProvider.AppContext = WeakReference(RuntimeEnvironment.getApplication())
    logsDir = File(RuntimeEnvironment.getApplication().filesDir, "logs")
    logsDir.deleteRecursively()
  }

  @After
  fun clearInstalledContext() {
    AndroidAPIProvider.AppContext = WeakReference(null)
  }

  @Test
  fun append_withoutInstalledContext_throws() {
    AndroidAPIProvider.AppContext = WeakReference(null)
    val appender = RollingFileAppender().apply { file = "NoContextLog" }

    assertThrows(IllegalArgumentException::class.java) { appender.append(loggingEvent("hello")) }
  }

  @Test
  fun append_inMainProcess_createsLogDirAndBaseFile() {
    val appender = RollingFileAppender().apply { file = "MainProcessLog" }

    appender.append(loggingEvent("hello"))

    assertTrue("logs directory should be created", logsDir.isDirectory)
    val logFile = File(logsDir, "MainProcessLog.txt")
    assertTrue("log file should be created with the base name", logFile.isFile)
    assertEquals("hello", logFile.readText().trim())
  }

  @Test
  fun append_inMainProcess_doesNotAddProcessSuffix() {
    val appender = RollingFileAppender().apply { file = "NoSuffixLog" }

    appender.append(loggingEvent("first"))

    val unexpectedSuffixed =
        logsDir.listFiles().orEmpty().filter {
          it.name.startsWith("NoSuffixLog_") && it.name.endsWith(".txt")
        }
    assertTrue(
        "no per-process suffixed file should be created in the main process",
        unexpectedSuffixed.isEmpty(),
    )
  }

  private fun loggingEvent(message: String): LoggingEvent {
    val event = mockk<LoggingEvent>()
    every { event.message } returns message
    return event
  }
}
