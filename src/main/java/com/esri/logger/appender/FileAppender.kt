package com.esri.logger.appender

import com.esri.logger.android.AndroidAPIProvider
import com.esri.logger.deleteExcessFiles
import com.esri.logger.encoder.Encoder
import com.esri.logger.incrementNumberedFileName
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import org.slf4j.event.LoggingEvent

class FileAppender : Appender {

  companion object {
    private const val FILENAME = "BridgeLog"
    private const val EXTENSION = ".txt"
    private const val MAX_FILES = 6
  }

  override var encoder: Encoder? = null

  private var bufferedWriter: BufferedWriter?

  init {
    val appDir = AndroidAPIProvider.filesDir
    val logDir = File("$appDir/logs")
    if (!logDir.exists()) {
      logDir.mkdir()
    }

    logDir.listFiles()?.toMutableList()?.apply {
      sortDescending()
      deleteExcessFiles(MAX_FILES - 1)
      forEach { file -> file.incrementNumberedFileName(FILENAME, EXTENSION) }
    }

    val logFile = File("${logDir}/$FILENAME$EXTENSION")
    logFile.createNewFile()
    bufferedWriter = BufferedWriter(FileWriter(logFile))
  }

  fun close() {
    bufferedWriter?.close()
    bufferedWriter = null
  }

  override fun append(event: LoggingEvent) {
    bufferedWriter?.apply {
      write(encoder?.encode(event)?.decodeToString() ?: event.message)
      write("\n")
      flush()
    } ?: run { throw Exception("Must initialize log file before using appender") }
  }
}
