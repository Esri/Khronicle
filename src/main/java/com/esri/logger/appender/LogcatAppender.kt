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

package com.esri.logger.appender

import android.util.Log
import com.esri.logger.encoder.Encoder
import org.slf4j.MarkerFactory
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent

class LogcatAppender : Appender {

  val defaultMarker = MarkerFactory.getMarker("None")

  override var encoder: Encoder? = null

  override fun append(event: LoggingEvent) {
    val message = encoder?.encode(event)?.decodeToString() ?: event.message
    val marker = event.markers?.firstOrNull() ?: defaultMarker
    when (event.level) {
      Level.ERROR -> Log.e(marker.name, message)
      Level.WARN -> Log.w(marker.name, message)
      Level.INFO -> Log.i(marker.name, message)
      Level.DEBUG -> Log.d(marker.name, message)
      Level.TRACE -> Log.v(marker.name, message)
      else -> Log.v(marker.name, message)
    }
  }
}
