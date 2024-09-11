package com.esri.logger.appender

import com.esri.logger.encoder.Encoder
import org.slf4j.event.LoggingEvent

class TestAppender : Appender {
  override var encoder: Encoder? = null
  var output: ((LoggingEvent) -> Unit)? = null

  override fun append(event: LoggingEvent) {
    output?.let { it(event) }
  }
}
