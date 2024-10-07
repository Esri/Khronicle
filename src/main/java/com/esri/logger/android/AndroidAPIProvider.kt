package com.esri.logger.android

import android.content.Context
import java.io.File
import java.lang.ref.WeakReference

class AndroidAPIProvider {

  companion object {
    var AppContext: WeakReference<Context?> = WeakReference(null)

    val filesDir: File?
      get() = AppContext.get()?.filesDir
  }
}
