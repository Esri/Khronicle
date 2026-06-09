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

package com.esri.logger.khronicle.android

import android.app.Application
import java.io.File

object AndroidAPIProvider {
  var AppContext: Application? = null

  fun installAppContext(application: Application) {
    AppContext = application
  }

  val filesDir: File?
    get() = AppContext?.filesDir

  internal fun resolveLogFileName(baseFileName: String): String {
    val context = AppContext ?: return baseFileName
    return AndroidProcess.resolveLogFileName(
        baseFileName = baseFileName,
        packageName = context.packageName,
        processName = AndroidProcess.currentProcessName(context),
    )
  }
}
