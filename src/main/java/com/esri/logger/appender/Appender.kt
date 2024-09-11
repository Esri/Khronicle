package com.esri.logger.appender

import com.esri.logger.encoder.Encoder
import org.slf4j.event.LoggingEvent

interface Appender {
  var encoder: Encoder?

  fun append(event: LoggingEvent)
}
