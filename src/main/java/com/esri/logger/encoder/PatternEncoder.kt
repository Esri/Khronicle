package com.esri.logger.encoder

import java.text.SimpleDateFormat
import java.util.Locale
import org.slf4j.event.LoggingEvent

class PatternEncoder(private val pattern: String) : Encoder {

  private val dateFormatter = SimpleDateFormat("MM-dd-yyyy HH:mm:ss,SSS", Locale.ENGLISH)

  override fun encode(loggingEvent: LoggingEvent): ByteArray {
    val formatMessage =
        pattern
            .replace("%level", loggingEvent.level.toString())
            .replace("%message", loggingEvent.message)
            .replace("%marker", loggingEvent.markers?.toString() ?: "")
            .replace("%date", dateFormatter.format(loggingEvent.timeStamp))
            .run { this + (loggingEvent.throwable?.let { "\n$it" } ?: "") }

    val reversedArgs = loggingEvent.arguments?.asReversed()
    val message =
        reversedArgs?.foldRight(formatMessage) { arg, message ->
          if (arg == null) {
            message.replaceFirst("{}", "null")
          } else {
            message.replaceFirst("{}", arg.toString())
          }
        } ?: formatMessage

    return message.toByteArray()
  }
}
