package com.esri.logger

import androidx.core.text.isDigitsOnly
import java.io.File

internal fun File.incrementNumberedFileName(namePrefix: String, extension: String) {
  val indexString = name.removeSurrounding(namePrefix, extension)
  val newIndex =
      if (indexString.isEmpty()) {
        1
      } else if (indexString.isDigitsOnly()) {
        indexString.toInt() + 1
      } else {
        throw IllegalArgumentException(
            "Input file did not follow required prefix#extension pattern")
      }

  renameTo(File(path, "$namePrefix$newIndex$extension"))
}

internal fun MutableList<File>.deleteExcessFiles(maxCount: Int) {
  while (size > maxCount) {
    val first = first()
    first.delete()
    remove(first)
  }
}
