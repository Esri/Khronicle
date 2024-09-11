package com.esri.logger

import com.esri.logger.appender.Appender
import org.slf4j.Logger
import org.slf4j.Marker
import org.slf4j.event.Level
import java.util.LinkedList

class LoggerImpl(private val loggerName: String) : Logger {
    var logLevel = Level.TRACE
    val appenders = LinkedList<Appender>()

    override fun getName(): String {
        return loggerName
    }

    override fun isTraceEnabled(marker: Marker?): Boolean {
        // Markers are not implemented
        return isTraceEnabled
    }

    private fun log(loggingEvent: LoggingEvent) {
        appenders.forEach { it.append(loggingEvent) }
    }

    private fun log(level: Level, format: String, arguments: MutableList<Any?>? = null) {
        if (level > logLevel) return

        val loggingEvent = LoggingEvent().apply {
            this.level = level
            this.message = format
            this.arguments = arguments
        }

        log(loggingEvent)
    }

    private fun log(level: Level, msg: String, t: Throwable?) {
        if (level > logLevel) return
        log(level, "$msg\n${t?.stackTrace ?: "null" }")
    }

    override fun isTraceEnabled(): Boolean {
        return logLevel.toInt() <= Level.TRACE.toInt()
    }

    override fun trace(msg: String?) {
        msg?.let { log(Level.TRACE, it) }
    }

    override fun trace(format: String?, arg: Any?) {
        if (format == null) return

        log(Level.TRACE, format, mutableListOf(arg))
    }

    override fun trace(format: String?, arg1: Any?, arg2: Any?) {
        if (format == null) return

        log(Level.TRACE, format, mutableListOf(arg1, arg2))
    }

    override fun trace(format: String?, vararg arguments: Any?) {
        if (format == null) return

        log(Level.TRACE, format, arguments.toMutableList())
    }

    override fun trace(msg: String?, t: Throwable?) {
        if (msg == null) return

        if (t == null) {
            // This is a weird case: if `t` is null, we don't know if it was meant to be a Throwable
            // or if it was a normal object. Let's handle it as if it was a null object.
            log(Level.TRACE, msg, mutableListOf(null))
        } else {
            log(Level.TRACE, msg, t)
        }
    }

    override fun trace(marker: Marker?, msg: String?) {
        // Markers are not implemented
        trace(msg)
    }

    override fun trace(marker: Marker?, format: String?, arg: Any?) {
        // Markers are not implemented
        trace(format, arg)
    }

    override fun trace(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        // Markers are not implemented
        trace(format, arg1, arg2)
    }

    override fun trace(marker: Marker?, format: String?, vararg arguments: Any?) {
        // Markers are not implemented
        trace(format, arguments)
    }

    override fun trace(marker: Marker?, msg: String?, t: Throwable?) {
        // Markers are not implemented
        trace(msg, t)
    }

    override fun isDebugEnabled(): Boolean {
        return logLevel <= Level.DEBUG
    }

    override fun isDebugEnabled(marker: Marker?): Boolean {
        // Markers are not implemented
        return isDebugEnabled
    }

    override fun debug(msg: String?) {
        msg?.let { log(Level.DEBUG, msg) }
    }

    override fun debug(format: String?, arg: Any?) {
        if (format == null) return

        log(Level.DEBUG, format, mutableListOf(arg))
    }

    override fun debug(format: String?, arg1: Any?, arg2: Any?) {
        if (format == null) return

        log(Level.DEBUG, format, mutableListOf(arg1, arg2))
    }

    override fun debug(format: String?, vararg arguments: Any?) {
        if (format == null) return

        log(Level.DEBUG, format, arguments.toMutableList())
    }

    override fun debug(msg: String?, t: Throwable?) {
        if (msg == null) return

        if (t == null) {
            // This is a weird case: if `t` is null, we don't know if it was meant to be a Throwable
            // or if it was a normal object. Let's handle it as if it was a null object.
            log(Level.DEBUG, msg, mutableListOf(null))
        } else {
            log(Level.DEBUG, msg, t)
        }
    }

    override fun debug(marker: Marker?, msg: String?) {
        // Markers are not implemented
        debug(msg)
    }

    override fun debug(marker: Marker?, format: String?, arg: Any?) {
        // Markers are not implemented
        debug(format, arg)
    }

    override fun debug(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        // Markers are not implemented
        debug(format, arg1, arg2)
    }

    override fun debug(marker: Marker?, format: String?, vararg arguments: Any?) {
        // Markers are not implemented
        debug(format, arguments)
    }

    override fun debug(marker: Marker?, msg: String?, t: Throwable?) {
        // Markers are not implemented
        debug(msg, t)
    }

    override fun isInfoEnabled(): Boolean {
        return logLevel <= Level.INFO
    }

    override fun isInfoEnabled(marker: Marker?): Boolean {
        // Markers are not implemented
        return isInfoEnabled
    }

    override fun info(msg: String?) {
        msg?.let { log(Level.INFO, it) }
    }

    override fun info(format: String?, arg: Any?) {
        if (format == null) return

        log(Level.INFO, format, mutableListOf(arg))
    }

    override fun info(format: String?, arg1: Any?, arg2: Any?) {
        if (format == null) return

        log(Level.INFO, format, mutableListOf(arg1, arg2))
    }

    override fun info(format: String?, vararg arguments: Any?) {
        if (format == null) return

        log(Level.INFO, format, arguments.toMutableList())
    }

    override fun info(msg: String?, t: Throwable?) {
        if (msg == null) return

        if (t == null) {
            // This is a weird case: if `t` is null, we don't know if it was meant to be a Throwable
            // or if it was a normal object. Let's handle it as if it was a null object.
            log(Level.INFO, msg, mutableListOf(null))
        } else {
            log(Level.INFO, msg, t)
        }
    }

    override fun info(marker: Marker?, msg: String?) {
        // Markers are not implemented
        info(msg)
    }

    override fun info(marker: Marker?, format: String?, arg: Any?) {
        // Markers are not implemented
        info(format, arg)
    }

    override fun info(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        // Markers are not implemented
        info(format, arg1, arg2)
    }

    override fun info(marker: Marker?, format: String?, vararg arguments: Any?) {
        // Markers are not implemented
        info(format, arguments)
    }

    override fun info(marker: Marker?, msg: String?, t: Throwable?) {
        // Markers are not implemented
        info(msg, t)
    }

    override fun isWarnEnabled(): Boolean {
        return logLevel <= Level.WARN
    }

    override fun isWarnEnabled(marker: Marker?): Boolean {
        // Markers are not implemented
        return isWarnEnabled
    }

    override fun warn(msg: String?) {
        msg?.let { log(Level.WARN, msg) }
    }

    override fun warn(format: String?, arg: Any?) {
        if (format == null) return
        if (arg == null) return

        log(Level.WARN, format, mutableListOf(arg))
    }

    override fun warn(format: String?, arg1: Any?, arg2: Any?) {
        if (format == null) return

        log(Level.WARN, format, mutableListOf(arg1, arg2))
    }

    override fun warn(format: String?, vararg arguments: Any?) {
        if (format == null) return

        log(Level.WARN, format, arguments.toMutableList())
    }

    override fun warn(msg: String?, t: Throwable?) {
        if (msg == null) return

        if (t == null) {
            // This is a weird case: if `t` is null, we don't know if it was meant to be a Throwable
            // or if it was a normal object. Let's handle it as if it was a null object.
            log(Level.WARN, msg, mutableListOf(null))
        } else {
            log(Level.WARN, msg, t)
        }
    }

    override fun warn(marker: Marker?, msg: String?) {
        // Markers are not implemented
        warn(msg)
    }

    override fun warn(marker: Marker?, format: String?, arg: Any?) {
        // Markers are not implemented
        warn(format, arg)
    }

    override fun warn(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        // Markers are not implemented
        warn(format, arg1, arg2)
    }

    override fun warn(marker: Marker?, format: String?, vararg arguments: Any?) {
        // Markers are not implemented
        warn(format, arguments)
    }

    override fun warn(marker: Marker?, msg: String?, t: Throwable?) {
        // Markers are not implemented
        warn(msg, t)
    }

    override fun isErrorEnabled(): Boolean {
        return logLevel <= Level.ERROR
    }

    override fun isErrorEnabled(marker: Marker?): Boolean {
        // Markers are not implemented
        return isErrorEnabled
    }

    override fun error(msg: String?) {
        msg?.let { log(Level.ERROR, msg) }
    }

    override fun error(format: String?, arg: Any?) {
        if (format == null) return

        log(Level.ERROR, format, mutableListOf(arg))
    }

    override fun error(format: String?, arg1: Any?, arg2: Any?) {
        if (format == null) return

        log(Level.ERROR, format, mutableListOf(arg1, arg2))
    }

    override fun error(format: String?, vararg arguments: Any?) {
        if (format == null) return

        log(Level.ERROR, format, arguments.toMutableList())
    }

    override fun error(msg: String?, t: Throwable?) {
        if (msg == null) return

        if (t == null) {
            // This is a weird case: if `t` is null, we don't know if it was meant to be a Throwable
            // or if it was a normal object. Let's handle it as if it was a null object.
            log(Level.ERROR, msg, mutableListOf(null))
        } else {
            log(Level.ERROR, msg, t)
        }
    }

    override fun error(marker: Marker?, msg: String?) {
        // Markers are not implemented
        error(msg)
    }

    override fun error(marker: Marker?, format: String?, arg: Any?) {
        // Markers are not implemented
        error(format, arg)
    }

    override fun error(marker: Marker?, format: String?, arg1: Any?, arg2: Any?) {
        // Markers are not implemented
        error(format, arg1, arg2)
    }

    override fun error(marker: Marker?, format: String?, vararg arguments: Any?) {
        // Markers are not implemented
        error(format, arguments)
    }

    override fun error(marker: Marker?, msg: String?, t: Throwable?) {
        // Markers are not implemented
        error(msg, t)
    }
}