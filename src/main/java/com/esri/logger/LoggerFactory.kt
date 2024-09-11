package com.esri.logger

import org.slf4j.ILoggerFactory
import org.slf4j.Logger

class LoggerFactory : ILoggerFactory {
  private val loggers = HashMap<String, Logger>()

  override fun getLogger(name: String): Logger {
    // For now we only support one logger
    return loggers["root"]!!
  }

  fun prepareLoggers(root: Root) {
    val builder = LoggerBuilder("root").setLogLevel(root.level)
    root.appenders.forEach { builder.addAppender(it) }

    loggers["root"] = builder.build()
  }
}
