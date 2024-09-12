package com.esri.logger.appender

import com.esri.logger.encoder.Encoder
import org.slf4j.event.LoggingEvent

class PrintlnAppender : Appender {
  override var encoder: Encoder? = null

  override fun append(event: LoggingEvent) {
    encoder?.let { println(it.encode(event).decodeToString()) } ?: { println(event.message) }
  }
}
