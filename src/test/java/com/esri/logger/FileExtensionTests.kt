package com.esri.logger

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.io.File
import java.io.IOException
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FileExtensionsTest {

  @Test
  fun incrementNumberedFileName_withNoNumber_shouldNumberFileAsOne() {
    val file = mockk<File>()
    every { file.path } returns "root/TestFile.ext"
    every { file.name } returns "TestFile.ext"
    every { file.renameTo(any()) } returns true

    file.incrementNumberedFileName("TestFile", ".ext")

    verify { file.renameTo(File("root", "TestFile1.ext")) }
  }

  @Test
  fun incrementNumberedFileName_shouldIncrementIndexCorrectly() {
    val file = mockk<File>()
    every { file.path } returns "root/TestFile1.ext"
    every { file.name } returns "TestFile1.ext"
    every { file.renameTo(any()) } returns true

    file.incrementNumberedFileName("TestFile", ".ext")

    verify { file.renameTo(File("root", "TestFile2.ext")) }
  }

  @Test(expected = IllegalArgumentException::class)
  fun incrementNumberedFileName_withMissingPrefix_throwsIllegalArgumentException() {
    val file = mockk<File>()
    every { file.path } returns "root/1.ext"
    every { file.name } returns "1.ext"
    every { file.renameTo(any()) } returns true

    file.incrementNumberedFileName("prefix", ".ext")
  }

  @Test(expected = IllegalArgumentException::class)
  fun incrementNumberedFileName_withMissingSuffix_throwsIllegalArgumentException() {
    val file = mockk<File>()
    every { file.path } returns "root/TestFile1"
    every { file.name } returns "TestFile1"
    every { file.renameTo(any()) } returns true

    file.incrementNumberedFileName("TestFile", ".ext")
  }

  @Test(expected = IllegalArgumentException::class)
  fun incrementNumberedFileName_withNonNumericFile_throwsIllegalArgumentException() {
    val file = mockk<File>()
    every { file.path } returns "root/TestFileABCD.ext"
    every { file.name } returns "TestFileABCD.ext"
    every { file.renameTo(any()) } returns true

    file.incrementNumberedFileName("TestFile", ".ext")
  }

  @Test(expected = IOException::class)
  fun incrementNumberedFileName_withFailedRename_throwsIOException() {
    val file = mockk<File>()
    every { file.path } returns "root/TestFile1.ext"
    every { file.name } returns "TestFile1.ext"
    every { file.renameTo(any()) } returns false

    file.incrementNumberedFileName("TestFile", ".ext")
  }

  @Test
  fun deleteExcessFiles_shouldRemoveFirstExcessFile() {
    val file1 = mockk<File>()
    val file2 = mockk<File>()
    val fileList = mutableListOf(file1, file2)
    every { file1.delete() } returns true
    every { file2.delete() } returns true

    fileList.deleteExcessFiles(1)

    verify { file1.delete() }
    assertThat("File list should now be size 1", fileList.size == 1)
    assertThat("Second file should remain in list", fileList.first() == file2)
  }

  @Test
  fun deleteExcessFiles_shouldRemoveNoFiles() {
    val file1 = mockk<File>()
    val file2 = mockk<File>()
    val fileList = mutableListOf(file1, file2)
    every { file1.delete() } returns true
    every { file2.delete() } returns true

    fileList.deleteExcessFiles(2)

    assertThat("File list should remain same size", fileList.size == 2)
    verify(exactly = 0) {
      file1.delete()
      file2.delete()
    }
  }

  @Test
  fun rotateFilesInPath_updatesNamesAndDeletesExcessFiles() {

    fun MockFile(name: String, path: String): File {
      val file = mockk<File>()
      every { file.name } returns name
      every { file.path } returns path
      every { file.renameTo(any()) } returns true
      every { file.compareTo(any()) } answers { file.name.compareTo(firstArg<File>().name) }
      every { file.delete() } returns true
      return file
    }

    val logFile = MockFile("log.txt", "logs/log.txt")
    val log1File = MockFile("log1.txt", "logs/log1.txt")
    val log2File = MockFile("log2.txt", "logs/log2.txt")
    val misnamedFile = MockFile("notalog.txt", "logs/notalog.txt")

    val mockDirectory = mockk<File>()
    val fileList = mutableListOf(logFile, log1File, log2File, misnamedFile)
    every { mockDirectory.listFiles() } returns fileList.toTypedArray()

    // Test
    mockDirectory.rotateFilesInPath("log", ".txt", 3)

    // Assert
    verify { logFile.renameTo(File("logs", "log1.txt")) }
    verify { log1File.renameTo(File("logs", "log2.txt")) }
    verify { log2File.delete() }
    verify(exactly = 0) {
      misnamedFile.delete()
      misnamedFile.renameTo(any())
    }
  }

  @Test
  fun matchesNumberedFilePattern_exhibitsExpectedRegexBehaviors() {
    val filename = "log"
    val extension = ".txt"

    assertThat(
        "File name expected to pass regex matcher",
        File("log.txt").matchesNumberedFilePattern(filename, extension))
    assertThat(
        "File name expected to pass regex matcher",
        File("log1.txt").matchesNumberedFilePattern(filename, extension))

    assertThat(
        "File name expected to not pass regex matcher",
        File("notlog.txt").matchesNumberedFilePattern(filename, extension) == false)
    assertThat(
        "File name expected to not pass regex matcher",
        File("lognot.txt").matchesNumberedFilePattern(filename, extension) == false)
    assertThat(
        "File name expected to not pass regex matcher",
        File("log.txtnot").matchesNumberedFilePattern(filename, extension) == false)
    assertThat(
        "File name expected to not pass regex matcher",
        File("log.nottxt").matchesNumberedFilePattern(filename, extension) == false)
  }
}
