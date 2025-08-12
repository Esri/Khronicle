// Copyright 2025 Esri
// 
// Licensed under the Apache License Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.esri.logger.khronicle

import androidx.core.text.isDigitsOnly
import java.io.File
import java.io.IOException

/**
 * Increments a numeric key in a file name that follows the pattern `namePrefix#.extension`
 *
 * e.g. if the file is named `log1.txt`, `namePrefix` is `log` and the `extension` is `.txt`, the
 * file will be renamed to `log2.txt`
 *
 * @param namePrefix The prefix that the file name should start with.
 * @param extension The file extension that the file should end with, including a `.`
 * @throws IllegalArgumentException If the file name does not follow the required pattern.
 * @throws IOException If the renaming operation fails.
 */
internal fun File.incrementNumberedFileName(namePrefix: String, extension: String) {
  val indexString = name.removeSurrounding(namePrefix, extension)
  val newIndex =
      when {
        indexString.isEmpty() -> 1
        indexString.isDigitsOnly() -> indexString.toInt() + 1
        else ->
            throw IllegalArgumentException(
                "Input file did not follow required prefix#extension pattern")
      }

  val newFile = File(path.removeSuffix(name), "$namePrefix$newIndex$extension")

  val result = renameTo(newFile)
  if (!result) {
    throw IOException("Rename failed")
  }
}

/**
 * Given a list of files, this method will prune from the front of the list ensuring that only
 * `maxCount` items remain in the list.
 *
 * This will both modify the list itself, and remove those items from the file system.
 */
internal fun MutableList<File>.deleteExcessFiles(maxCount: Int) {
  while (size > maxCount) {
    val first = first()
    first.delete()
    remove(first)
  }
}

/**
 * Examines files in a given directory and prunes files to ensure the count is 1 less than the
 * specified max to make room for a new file. Additionally this will increment a numeric keys in the
 * file names following the pattern `filename#.extension`
 *
 * e.g:
 * - A directory containing files `["log.txt", "log1,txt", "log2.txt"]`
 * - With params `maxFiles = 3`, `filename = "log"`, `extension = ".txt"`
 *
 * Would result in: "log2.txt" becomes deleted, "log1.txt" becomes "log2.txt", and "log.txt" becomes
 * "log1.txt"
 */
internal fun File.rotateFilesInPath(filename: String, extension: String, maxFiles: Int) {
  listFiles()
      ?.filter { it.matchesNumberedFilePattern(filename, extension) }
      ?.toMutableList()
      ?.apply {
        sortDescending()
        deleteExcessFiles(maxFiles - 1)
        forEach { file -> file.incrementNumberedFileName(filename, extension) }
      }
}

internal fun File.matchesNumberedFilePattern(filename: String, extension: String): Boolean {
  return Regex("^$filename\\d*\\$extension$").matches(name)
}
