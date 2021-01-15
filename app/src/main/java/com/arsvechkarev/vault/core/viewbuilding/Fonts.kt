package com.arsvechkarev.vault.core.viewbuilding

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import java.util.concurrent.CountDownLatch

object Fonts {
  
  private val initializationLatch = CountDownLatch(1)
  
  private var segoeUi: Typeface? = null
  private var segoeUiBold: Typeface? = null
  
  val SegoeUi: Typeface
    get() {
      initializationLatch.await()
      return segoeUi!!
    }
  
  val SegoeUiBold: Typeface
    get() {
      initializationLatch.await()
      return segoeUiBold!!
    }
  
  fun init(context: Context) {
    AndroidThreader.onBackground {
      segoeUi = ResourcesCompat.getFont(context, R.font.segoe_ui)
      segoeUiBold = ResourcesCompat.getFont(context, R.font.segoe_ui_bold)
      initializationLatch.countDown()
    }
  }
}