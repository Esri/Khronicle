package com.esri.logger

import com.esri.logger.appender.Appender
import java.util.LinkedList
import org.slf4j.Logger
import org.slf4j.Marker
import org.slf4j.event.Level

class LoggerImpl(private val loggerName: String) : Logger {
  var logLevel = Level.TRACE
  val appenders = LinkedList<Appender>()

  override fun getName(): String {
    return loggerName
  }

  private fun log(loggingEvent: LoggingEvent) {
    appenders.forEach { it.append(loggingEvent) }
  }

  private fun log(
      level: Level,
      format: String?,
      arguments: MutableList<Any?>? = null,
      marker: Marker? = null,
      throwable: Throwable? = null
  ) {
    if (format == null) return
    if (level > logLevel) return

    val internalThrowable =
        if (throwable == null && arguments?.last() is Throwable) {
          val lastThrowable = (arguments.last() as Throwable)
          arguments.removeAt(arguments.lastIndex)
          lastThrowable
        } else {
          throwable
        }

    val loggingEvent =
        LoggingEvent().apply {
          this.level = level
          this.message = format
          this.arguments = arguments
          this.markers = marker?.let { mutableListOf(it) }
          this.throwable = internalThrowable
          this.timeStamp = System.currentTimeMillis()
        }

    log(loggingEvent)
  }

  override fun isTraceEnabled(marker: Marker?): Boolean {
    // Marker filters are not implemented
    return isTraceEnabled
  }

  override fun isTraceEnabled(): Boolean {
    return logLevel.toInt() <= Level.TRACE.toInt()
  }

  override fun trace(msg: String?) = log(level = Level.TRACE, format = msg)

  override fun trace(format: String?, arg: Any?) =
      log(level = Level.TRACE, format = format, arguments = mutableListOf(arg))

  override fun trace(format: String?, arg1: Any?, arg2: Any?) =
      log(level = Level.TRACE, format = format, arguments = mutableListOf(arg1, arg2))

  override fun trace(format: String?, vararg arguments: Any?) =
      log(level = Level.TRACE, format = format, arguments = arguments.toMutableList())

  override fun trace(msg: String?, t: Throwable?) =
      log(level = Level.TRACE, format = msg, throwable = t)

  override fun trace(marker: Marker?, msg: String?) =
      log(level = Level.TRACE, format = msg, marker = marker)

  override fun trace(marker: Marker?, format: String?, arg: Any?) =
      log(level = Level.TRACE, format = format, marker = marker, arguments = mutableListOf(arg))

  override fun trace(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) =
      log(
          level = Level.TRACE,
          format = format,
          marker = marker,
          arguments = mutableListOf(arg1, arg2))

  override fun trace(marker: Marker?, format: String?, vararg arguments: Any?) =
      log(
          level = Level.TRACE,
          format = format,
          marker = marker,
          arguments = arguments.toMutableList())

  override fun trace(marker: Marker?, msg: String?, t: Throwable?) =
      log(level = Level.TRACE, format = msg, marker = marker, throwable = t)

  override fun isDebugEnabled(): Boolean {
    return logLevel <= Level.DEBUG
  }

  override fun isDebugEnabled(marker: Marker?): Boolean {
    // Marker filters are not implemented
    return isDebugEnabled
  }

  override fun debug(msg: String?) = log(level = Level.DEBUG, format = msg)

  override fun debug(format: String?, arg: Any?) =
      log(level = Level.DEBUG, format = format, arguments = mutableListOf(arg))

  override fun debug(format: String?, arg1: Any?, arg2: Any?) =
      log(level = Level.DEBUG, format = format, arguments = mutableListOf(arg1, arg2))

  override fun debug(format: String?, vararg arguments: Any?) =
      log(level = Level.DEBUG, format = format, arguments = arguments.toMutableList())

  override fun debug(msg: String?, t: Throwable?) =
      log(level = Level.DEBUG, format = msg, throwable = t)

  override fun debug(marker: Marker?, msg: String?) =
      log(level = Level.DEBUG, format = msg, marker = marker)

  override fun debug(marker: Marker?, format: String?, arg: Any?) =
      log(level = Level.DEBUG, format = format, marker = marker, arguments = mutableListOf(arg))

  override fun debug(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) =
      log(
          level = Level.DEBUG,
          format = format,
          marker = marker,
          arguments = mutableListOf(arg1, arg2))

  override fun debug(marker: Marker?, format: String?, vararg arguments: Any?) =
      log(
          level = Level.DEBUG,
          format = format,
          marker = marker,
          arguments = arguments.toMutableList())

  override fun debug(marker: Marker?, msg: String?, t: Throwable?) =
      log(level = Level.DEBUG, format = msg, marker = marker, throwable = t)

  override fun isInfoEnabled(): Boolean {
    return logLevel <= Level.INFO
  }

  override fun isInfoEnabled(marker: Marker?): Boolean {
    // Marker filters are not implemented
    return isInfoEnabled
  }

  override fun info(msg: String?) = log(level = Level.INFO, format = msg)

  override fun info(format: String?, arg: Any?) =
      log(level = Level.INFO, format = format, arguments = mutableListOf(arg))

  override fun info(format: String?, arg1: Any?, arg2: Any?) =
      log(level = Level.INFO, format = format, arguments = mutableListOf(arg1, arg2))

  override fun info(format: String?, vararg arguments: Any?) =
      log(level = Level.INFO, format = format, arguments = arguments.toMutableList())

  override fun info(msg: String?, t: Throwable?) =
      log(level = Level.INFO, format = msg, throwable = t)

  override fun info(marker: Marker?, msg: String?) =
      log(level = Level.INFO, format = msg, marker = marker)

  override fun info(marker: Marker?, format: String?, arg: Any?) =
      log(level = Level.INFO, format = format, marker = marker, arguments = mutableListOf(arg))

  override fun info(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) =
      log(
          level = Level.INFO,
          format = format,
          marker = marker,
          arguments = mutableListOf(arg1, arg2))

  override fun info(marker: Marker?, format: String?, vararg arguments: Any?) =
      log(
          level = Level.INFO,
          format = format,
          marker = marker,
          arguments = arguments.toMutableList())

  override fun info(marker: Marker?, msg: String?, t: Throwable?) =
      log(level = Level.INFO, format = msg, marker = marker, throwable = t)

  override fun isWarnEnabled(): Boolean {
    return logLevel <= Level.WARN
  }

  override fun isWarnEnabled(marker: Marker?): Boolean {
    // Marker filters are not implemented
    return isWarnEnabled
  }

  override fun warn(msg: String?) = log(level = Level.WARN, format = msg)

  override fun warn(format: String?, arg: Any?) =
      log(level = Level.WARN, format = format, arguments = mutableListOf(arg))

  override fun warn(format: String?, arg1: Any?, arg2: Any?) =
      log(level = Level.WARN, format = format, arguments = mutableListOf(arg1, arg2))

  override fun warn(format: String?, vararg arguments: Any?) =
      log(level = Level.WARN, format = format, arguments = arguments.toMutableList())

  override fun warn(msg: String?, t: Throwable?) =
      log(level = Level.WARN, format = msg, throwable = t)

  override fun warn(marker: Marker?, msg: String?) =
      log(level = Level.WARN, format = msg, marker = marker)

  override fun warn(marker: Marker?, format: String?, arg: Any?) =
      log(level = Level.WARN, format = format, marker = marker, arguments = mutableListOf(arg))

  override fun warn(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) =
      log(
          level = Level.WARN,
          format = format,
          marker = marker,
          arguments = mutableListOf(arg1, arg2))

  override fun warn(marker: Marker?, format: String?, vararg arguments: Any?) =
      log(
          level = Level.WARN,
          format = format,
          marker = marker,
          arguments = arguments.toMutableList())

  override fun warn(marker: Marker?, msg: String?, t: Throwable?) =
      log(level = Level.WARN, format = msg, marker = marker, throwable = t)

  override fun isErrorEnabled(): Boolean {
    return logLevel <= Level.ERROR
  }

  override fun isErrorEnabled(marker: Marker?): Boolean {
    // Marker filters are not implemented
    return isErrorEnabled
  }

  override fun error(msg: String?) = log(level = Level.ERROR, format = msg)

  override fun error(format: String?, arg: Any?) =
      log(level = Level.ERROR, format = format, arguments = mutableListOf(arg))

  override fun error(format: String?, arg1: Any?, arg2: Any?) =
      log(level = Level.ERROR, format = format, arguments = mutableListOf(arg1, arg2))

  override fun error(format: String?, vararg arguments: Any?) =
      log(level = Level.ERROR, format = format, arguments = arguments.toMutableList())

  override fun error(msg: String?, t: Throwable?) =
      log(level = Level.ERROR, format = msg, throwable = t)

  override fun error(marker: Marker?, msg: String?) =
      log(level = Level.ERROR, format = msg, marker = marker)

  override fun error(marker: Marker?, format: String?, arg: Any?) =
      log(level = Level.ERROR, format = format, marker = marker, arguments = mutableListOf(arg))

  override fun error(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) =
      log(
          level = Level.ERROR,
          format = format,
          marker = marker,
          arguments = mutableListOf(arg1, arg2))

  override fun error(marker: Marker?, format: String?, vararg arguments: Any?) =
      log(
          level = Level.ERROR,
          format = format,
          marker = marker,
          arguments = arguments.toMutableList())

  override fun error(marker: Marker?, msg: String?, t: Throwable?) =
      log(level = Level.ERROR, format = msg, marker = marker, throwable = t)
}
