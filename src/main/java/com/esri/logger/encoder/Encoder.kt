package com.esri.logger.encoder

import org.slf4j.event.LoggingEvent

interface Encoder {
  fun encode(loggingEvent: LoggingEvent): ByteArray
}
