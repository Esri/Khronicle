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
  companion object {
    const val ATTR_CLASS = "class"
    const val ATTR_LEVEL = "level"
    const val ATTR_NAME = "name"
    const val ATTR_REF = "ref"
    const val TAG_APPENDER = "appender"
    const val TAG_APPENDER_REF = "appender-ref"
    const val TAG_CONFIGURATION = "configuration"
    const val TAG_ENCODER = "encoder"
    const val TAG_PATTERN = "pattern"
    const val TAG_ROOT = "root"
  }

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
    parser.require(XmlPullParser.START_TAG, null, TAG_CONFIGURATION)
    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType == XmlPullParser.END_DOCUMENT) {
        throw RuntimeException("Invalid config file!")
      }

      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        TAG_APPENDER -> handleAppender()
        TAG_ROOT -> handleRoot()
        else -> {
          println("Feed found unsupported tag: ${parser.name}")
          skip()
        }
      }
    }
  }

  private fun handleAppender() {
    parser.require(XmlPullParser.START_TAG, null, TAG_APPENDER)

    val appenderName = parser.getAttributeValue(null, ATTR_NAME)
    val appenderClass = parser.getAttributeValue(null, ATTR_CLASS)
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
        TAG_ENCODER -> handleEncoder(appender)
        else -> {
          println("Appender found unsupported tag: ${parser.name}")
          skip()
        }
      }
    }
  }

  private fun handleEncoder(appender: Appender) {
    parser.require(XmlPullParser.START_TAG, null, TAG_ENCODER)

    while (parser.next() != XmlPullParser.END_TAG) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        TAG_PATTERN -> appender.encoder = PatternEncoder(readText())
        else -> {
          println("Encoder found unsupported tag: ${parser.name}")
          skip()
        }
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
    parser.require(XmlPullParser.START_TAG, null, TAG_ROOT)
    parser.getAttributeValue(null, ATTR_LEVEL)?.let { level -> root.setLevel(level) }

    while (!(parser.next() == XmlPullParser.END_TAG && parser.name == TAG_ROOT)) {
      if (parser.eventType != XmlPullParser.START_TAG) {
        continue
      }

      when (parser.name) {
        TAG_APPENDER_REF -> {
          appenders[parser.getAttributeValue(null, ATTR_REF)]?.let { root.appenders.add(it) }
        }
        else -> {
          println("Root found unsupported tag: ${parser.name}")
          skip()
        }
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
