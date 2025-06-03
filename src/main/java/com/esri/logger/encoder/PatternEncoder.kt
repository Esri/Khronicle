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

package com.esri.logger.encoder

import java.text.SimpleDateFormat
import java.util.Locale
import org.slf4j.event.LoggingEvent

class PatternEncoder(private val pattern: String) : Encoder {

  private val dateFormatter = SimpleDateFormat("MM-dd-yyyy HH:mm:ss,SSS", Locale.ENGLISH)

  override fun encode(loggingEvent: LoggingEvent): ByteArray {
    val formatMessage =
        pattern
            .replace("%level", loggingEvent.level.toString())
            .replace("%message", loggingEvent.message)
            .replace("%marker", loggingEvent.markers?.toString() ?: "")
            .replace("%date", dateFormatter.format(loggingEvent.timeStamp))
            .replace("%timestamp", loggingEvent.timeStamp.toString())
            .run { this + (loggingEvent.throwable?.let { "\n$it" } ?: "") }

    val reversedArgs = loggingEvent.arguments?.asReversed()
    val message =
        reversedArgs?.foldRight(formatMessage) { arg, message ->
          if (arg == null) {
            message.replaceFirst("{}", "null")
          } else {
            message.replaceFirst("{}", arg.toString())
          }
        } ?: formatMessage

    return message.toByteArray()
  }
}
