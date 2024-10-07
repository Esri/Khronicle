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
}
