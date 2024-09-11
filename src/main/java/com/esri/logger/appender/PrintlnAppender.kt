package com.esri.logger.appender

import com.esri.logger.encoder.Encoder
import org.slf4j.event.LoggingEvent

class PrintlnAppender : Appender {
    override var encoder: Encoder? = null

    override fun append(event: LoggingEvent) {
        if (encoder == null) {
            println(event.message)
        } else {
            println(encoder!!.encode(event).decodeToString())
        }
    }
}