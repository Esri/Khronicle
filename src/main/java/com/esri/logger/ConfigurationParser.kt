package com.esri.logger

import android.util.Xml
import com.esri.logger.appender.Appender
import com.esri.logger.appender.PrintlnAppender
import com.esri.logger.encoder.PatternEncoder
import java.io.InputStream
import java.util.LinkedList
import org.slf4j.event.Level
import org.xmlpull.v1.XmlPullParser

class ConfigurationParser {
  val root = Root()

  private val parser = Xml.newPullParser()
  private val appenders = HashMap<String, Appender>()

  fun parse(inputStream: InputStream) {
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
    parser.setInput(inputStream, null)
    parser.nextTag()
    readFeed()
  }

  private fun readFeed() {
    parser.require(XmlPullParser.START_TAG, null, "configuration")
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.END_DOCUMENT) {
        throw RuntimeException("Invalid config file!")
      }

      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        "appender" -> handleAppender()
        "root" -> handleRoot()
      }
    }
  }

  private fun handleAppender() {
    parser.require(XmlPullParser.START_TAG, null, "appender")

    val appenderName = parser.getAttributeValue(null, "name")
    val appenderClass = parser.getAttributeValue(null, "class")
    val appender =
        appenderClass?.let {
          Class.forName(appenderClass).getDeclaredConstructor().newInstance() as Appender
        } ?: PrintlnAppender()
    appenders[appenderName] = appender

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        "encoder" -> handleEncoder(appender)
      }
    }
  }

  private fun handleEncoder(appender: Appender) {
    parser.require(XmlPullParser.START_TAG, null, "encoder")

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        "pattern" -> appender.encoder = PatternEncoder(readText())
        else -> skip()
      }
    }
  }

  private fun readText(): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.text
      parser.nextTag()
    }
    return result
  }

  private fun handleRoot() {
    parser.require(XmlPullParser.START_TAG, null, "root")
    parser.getAttributeValue(null, "level")?.let { level -> root.setLevel(level) }

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        "appender-ref" -> {
          appenders[parser.getAttributeValue(null, "ref")]?.let { root.appenders.add(it) }
        }
        else -> skip()
      }
    }
  }

  private fun skip() {
    if (parser.eventType != XmlPullParser.START_TAG) {
      throw IllegalStateException()
    }

    var depth = 1
    while (depth != 0) {
      when (parser.next()) {
        XmlPullParser.END_TAG -> depth--
        XmlPullParser.START_TAG -> depth++
      }
    }
  }
}

class Root {
  var level: Level = DEFAULT_LEVEL
  val appenders = LinkedList<Appender>()

  fun setLevel(level: String) {
    when (level.uppercase()) {
      "TRACE" -> this.level = Level.TRACE
      "DEBUG" -> this.level = Level.DEBUG
      "INFO" -> this.level = Level.INFO
      "WARN" -> this.level = Level.WARN
      "ERROR" -> this.level = Level.ERROR
    }
  }

  companion object {
    val DEFAULT_LEVEL = Level.DEBUG
  }
}
