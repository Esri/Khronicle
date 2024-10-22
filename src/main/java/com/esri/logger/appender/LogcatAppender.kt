package com.esri.logger.appender

import android.util.Log
import com.esri.logger.encoder.Encoder
import org.slf4j.MarkerFactory
import org.slf4j.event.Level
import org.slf4j.event.LoggingEvent

class LogcatAppender : Appender {

  val defaultMarker = MarkerFactory.getMarker("None")

  override var encoder: Encoder? = null

  override fun append(event: LoggingEvent) {
    val message = encoder?.encode(event)?.decodeToString() ?: event.message
    val marker = event.markers?.firstOrNull() ?: defaultMarker
    when (event.level) {
      Level.ERROR -> Log.e(marker.name, message)
      Level.WARN -> Log.w(marker.name, message)
      Level.INFO -> Log.i(marker.name, message)
      Level.DEBUG -> Log.d(marker.name, message)
      Level.TRACE -> Log.v(marker.name, message)
      else -> Log.v(marker.name, message)
    }
  }
}
