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

import org.slf4j.ILoggerFactory
import org.slf4j.Logger

class LoggerFactory : ILoggerFactory {

  private val loggers = HashMap<String, Logger>()

  override fun getLogger(name: String): Logger {
    return loggers.getOrElse(name) {
      loggers["root"]
          ?: throw RuntimeException(
              "The root logger should have been prepared at initialization time. This should never happen.")
    }
  }

  fun prepareLoggers(loggers: Map<String, LoggerConfig>) {
    loggers.forEach { (name, config) ->
      val builder = LoggerBuilder(name).setLogLevel(config.level)
      config.appenders.forEach { builder.addAppender(it) }
      this.loggers[name] = builder.build()
    }
  }
}
