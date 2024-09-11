package com.esri.logger.appender

import com.esri.logger.encoder.Encoder
import org.slf4j.event.LoggingEvent

class FileAppender : Appender {
  override var encoder: Encoder? = null

  override fun append(event: LoggingEvent) {
    TODO("Not yet implemented")
  }
}
