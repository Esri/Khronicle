package com.esri.logger

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
