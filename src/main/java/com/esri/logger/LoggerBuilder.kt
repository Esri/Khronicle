package com.esri.logger

import com.esri.logger.appender.Appender
import org.slf4j.Logger
import org.slf4j.event.Level

class LoggerBuilder(name: String) {
  private val logger = LoggerImpl(name)

  fun setLogLevel(level: Level) = apply { logger.logLevel = level }

  fun addAppender(appender: Appender) = apply { logger.appenders.add(appender) }

  fun build(): Logger {
    return logger
  }
}
