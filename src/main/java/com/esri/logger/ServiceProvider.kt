package com.esri.logger

import org.slf4j.ILoggerFactory
import org.slf4j.IMarkerFactory
import org.slf4j.spi.MDCAdapter
import org.slf4j.spi.SLF4JServiceProvider

class ServiceProvider : SLF4JServiceProvider {
  private val loggerFactory = LoggerFactory()

  override fun getLoggerFactory(): ILoggerFactory {
    return loggerFactory
  }

  override fun getMarkerFactory(): IMarkerFactory {
    TODO("Not yet implemented")
  }

  override fun getMDCAdapter(): MDCAdapter {
    TODO("Not yet implemented")
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
    loggerFactory.prepareLoggers(configParser.root)
  }
}
