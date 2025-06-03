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

package com.esri.logger

import org.slf4j.Marker
import org.slf4j.event.KeyValuePair
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent

class LoggingEvent : LoggingEvent {
  private var level: Level? = null
  private var loggerName: String? = null
  private var message: String? = null
  private var arguments: MutableList<Any?>? = null
  private var markers: MutableList<Marker>? = null
  private var keyValuePairs: MutableList<KeyValuePair>? = null
  private var throwable: Throwable? = null
  private var timestamp: Long = -1
  private var threadName: String? = null

  override fun getLevel(): Level? {
    return level
  }

  fun setLevel(level: Level?) {
    this.level = level
  }

  override fun getLoggerName(): String? {
    return loggerName
  }

  fun setLoggerName(loggerName: String?) {
    this.loggerName = loggerName
  }

  override fun getMessage(): String? {
    return message
  }

  fun setMessage(message: String?) {
    this.message = message
  }

  override fun getArguments(): MutableList<Any?>? {
    return arguments
  }

  fun setArguments(arguments: MutableList<Any?>?) {
    this.arguments = arguments
  }

  override fun getArgumentArray(): Array<Any?>? {
    return arguments?.toTypedArray()
  }

  override fun getMarkers(): MutableList<Marker>? {
    return markers
  }

  fun setMarkers(markers: MutableList<Marker>?) {
    this.markers = markers
  }

  override fun getKeyValuePairs(): MutableList<KeyValuePair>? {
    return keyValuePairs
  }

  fun setKeyValuePairs(keyValuePairs: MutableList<KeyValuePair>?) {
    this.keyValuePairs = keyValuePairs
  }

  override fun getThrowable(): Throwable? {
    return throwable
  }

  fun setThrowable(throwable: Throwable?) {
    this.throwable = throwable
  }

  override fun getTimeStamp(): Long {
    return timestamp
  }

  fun setTimeStamp(timestamp: Long) {
    this.timestamp = timestamp
  }

  override fun getThreadName(): String? {
    return threadName
  }

  fun setThreadName(threadName: String?) {
    this.threadName = threadName
  }
}
