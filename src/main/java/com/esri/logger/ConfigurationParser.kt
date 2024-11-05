package com.esri.logger

import android.R.attr.propertyName
import android.util.Xml
import com.esri.logger.appender.Appender
import com.esri.logger.encoder.PatternEncoder
import java.io.InputStream
import java.util.LinkedList
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import org.slf4j.event.Level
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException

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
    const val TAG_LOGGER = "logger"
  }

  val loggers = mutableMapOf<String, LoggerConfig>().also { it["root"] = LoggerConfig() }

  private val parser = Xml.newPullParser()
  private val appenders = HashMap<String, Appender>()

  fun parse(inputStream: InputStream) {
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
    parser.setInput(inputStream, null)
    parser.nextTag()
    readFeed()
  }

  // region Logger-Android XML content readers

  private fun readFeed() =
      handleTag(parser, TAG_CONFIGURATION) {
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
            TAG_LOGGER -> handleLogger()
            else -> {
              println("Feed found unsupported tag: ${parser.name}")
              skip()
            }
          }
        }
      }

  private fun handleAppender() =
      handleTag(parser, TAG_APPENDER) {
        val appenderName = parser.getAttributeValue(null, ATTR_NAME)
        val appenderClass = parser.getAttributeValue(null, ATTR_CLASS)

        val kClass =
            appenderClass?.let { Class.forName(appenderClass).kotlin }
                ?: run {
                  throw XmlPullParserException("Appender with name \"$appenderClass\" not found")
                }

        val appender = kClass.createInstance() as Appender

        appenders[appenderName] = appender

        while (parser.next() != XmlPullParser.END_TAG) {
          if (parser.eventType != XmlPullParser.START_TAG) {
            continue
          }

          when (parser.name) {
            TAG_ENCODER -> handleEncoder(appender)
            else -> {
              val property =
                  kClass.memberProperties.find { it.name == parser.name }
                      as? KMutableProperty1<Any, Any?>
              if (property != null) {
                val contents = readText()
                property.set(appender, contents)
                println("Appender property ${parser.name} set to $contents")
                parser.next()
              } else {
                println("Appender property $propertyName not found")
                skip()
              }
            }
          }
        }
      }

  private fun handleEncoder(appender: Appender) =
      handleTag(parser, TAG_ENCODER) {
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

  private fun handleRoot() =
      handleTag(parser, TAG_ROOT) {
        val root = loggers["root"]

        if (root == null) {
          throw RuntimeException("Root config should be pre-initialized")
        }

        parser.getAttributeValue(null, ATTR_LEVEL)?.let { level -> root.setLevel(level) }

        while (parser.next() != XmlPullParser.END_TAG) {
          if (parser.eventType != XmlPullParser.START_TAG) {
            continue
          }

          when (parser.name) {
            TAG_APPENDER_REF -> {
              handleAppenderRef(root)
            }
            else -> {
              println("Root found unsupported tag: ${parser.name}")
              skip()
            }
          }
        }
      }

  private fun handleLogger() =
      handleTag(parser, TAG_LOGGER) {
        val logger =
            parser.getAttributeValue(null, ATTR_NAME)?.let { name ->
              val logger = LoggerConfig()
              loggers[name] = logger
              logger
            } ?: run { throw Exception("Logger configuration requires name attribute") }
        parser.getAttributeValue(null, ATTR_LEVEL)?.let { level -> logger.setLevel(level) }

        while (parser.next() != XmlPullParser.END_TAG) {
          if (parser.eventType != XmlPullParser.START_TAG) {
            continue
          }

          when (parser.name) {
            TAG_APPENDER_REF -> {
              handleAppenderRef(logger)
            }
            else -> {
              println("Root found unsupported tag: ${parser.name}")
              skip()
            }
          }
        }
      }

  private fun handleAppenderRef(logger: LoggerConfig) =
      handleTag(parser, TAG_APPENDER_REF) {
        appenders[parser.getAttributeValue(null, ATTR_REF)]?.let { logger.appenders.add(it) }
        parser.nextTag()
      }

  // endregion

  // region General XML readers

  private fun readText(): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
      result = parser.text
      parser.nextTag()
    }
    return result
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

  // endregion

  private fun handleTag(parser: XmlPullParser, name: String, body: () -> Unit) {
    parser.require(XmlPullParser.START_TAG, null, name)
    body.invoke()
    parser.require(XmlPullParser.END_TAG, null, name)
  }
}

class LoggerConfig {
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
