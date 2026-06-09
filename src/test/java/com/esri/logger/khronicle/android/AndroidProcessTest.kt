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

package com.esri.logger.khronicle.android

import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidProcessTest {

  @Test
  fun resolveLogFileName_withMainProcess_keepsBaseFileName() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = "com.example.app",
        )

    assertEquals("ExampleAppLog", fileName)
  }

  @Test
  fun resolveLogFileName_withPackageLocalProcess_addsLocalProcessSuffix() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = "com.example.app:worker",
        )

    assertEquals("ExampleAppLog_worker", fileName)
  }

  @Test
  fun resolveLogFileName_withExternalProcess_addsSanitizedFullProcessSuffix() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = "external/process:name",
        )

    assertEquals("ExampleAppLog_external_process_name", fileName)
  }

  @Test
  fun resolveLogFileName_withBlankProcess_keepsBaseFileName() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = "",
        )

    assertEquals("ExampleAppLog", fileName)
  }

  @Test
  fun resolveLogFileName_withNullProcess_keepsBaseFileName() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = null,
        )

    assertEquals("ExampleAppLog", fileName)
  }

  @Test
  fun resolveLogFileName_withEmptyLocalProcessSuffix_keepsBaseFileName() {
    val fileName =
        AndroidProcess.resolveLogFileName(
            baseFileName = "ExampleAppLog",
            packageName = "com.example.app",
            processName = "com.example.app:",
        )

    assertEquals("ExampleAppLog", fileName)
  }
}
