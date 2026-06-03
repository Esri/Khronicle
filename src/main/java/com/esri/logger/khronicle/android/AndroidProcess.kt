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

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process

internal object AndroidProcess {

  fun resolveLogFileName(baseFileName: String, packageName: String, processName: String?): String {
    return baseFileName + processSuffix(packageName, processName)
  }

  fun processSuffix(packageName: String, processName: String?): String {
    val processFileName =
        processName
            ?.takeUnless { it == packageName }
            ?.removePrefix("$packageName:")
            ?.sanitizeForFileName()
            .orEmpty()

    return if (processFileName.isBlank()) "" else "_$processFileName"
  }

  fun currentProcessName(context: Context): String? {
    val processNameFromApplication =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
          Application.getProcessName()?.takeIf { it.isNotBlank() }
        } else {
          null
        }

    val currentPid = Process.myPid()
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    val processNameFromActivityManager =
        activityManager
            ?.runningAppProcesses
            ?.firstOrNull { it.pid == currentPid }
            ?.processName
            ?.takeIf { it.isNotBlank() }

    return processNameFromApplication ?: processNameFromActivityManager
  }

  private fun String.sanitizeForFileName(): String {
    return replace(Regex("[^A-Za-z0-9._-]"), "_")
  }
}
