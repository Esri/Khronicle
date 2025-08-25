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
import org.slf4j.IMarkerFactory
import org.slf4j.helpers.BasicMarkerFactory
import org.slf4j.helpers.NOPMDCAdapter
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class ServiceProvider : SLF4JServiceProvider {
  private val loggerFactory = LoggerFactory()
  private val markerFactory = BasicMarkerFactory()
  private val mdcAdapter = NOPMDCAdapter()

  override fun getLoggerFactory(): ILoggerFactory {
    return loggerFactory
  }

  override fun getMarkerFactory(): IMarkerFactory {
    return markerFactory
  }

  override fun getMDCAdapter(): MDCAdapter {
    return mdcAdapter
  }

  override fun getRequestedApiVersion(): String {
    return "2.0.16"
  }

  override fun initialize() {
    val configParser = ConfigurationParser()
    parseConfig(configParser)
    prepareLoggers(configParser)
  }

  private fun parseConfig(configParser: ConfigurationParser) {
    this.javaClass.classLoader?.getResource("assets/logger-config.xml")?.let { configUrl ->
      val configUrlConnection = configUrl.openConnection()
      val inputStream = configUrlConnection.getInputStream()
      inputStream.use { configParser.parse(it) }
    }
  }

  private fun prepareLoggers(configParser: ConfigurationParser) {
    val loggerFactory = loggerFactory
    loggerFactory.prepareLoggers(configParser.loggers)
  }
}
