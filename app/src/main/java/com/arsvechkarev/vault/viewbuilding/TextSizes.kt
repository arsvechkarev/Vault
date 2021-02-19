package com.arsvechkarev.vault.viewbuilding

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewdsl.ContextHolder
import com.arsvechkarev.vault.viewdsl.dimen

object TextSizes {
  
  private val textSizes = FloatArray(8) { 0f }
  
  val PasswordStub: Float get() = textSizes[0]
  val H0: Float get() = textSizes[1]
  val H1: Float get() = textSizes[2]
  val H2: Float get() = textSizes[3]
  val H3: Float get() = textSizes[4]
  val H4: Float get() = textSizes[5]
  val H5: Float get() = textSizes[6]
  val H6: Float get() = textSizes[7]
  
  init {
    textSizes[0] = adjustText(R.dimen.text_password_stub)
    textSizes[1] = adjustText(R.dimen.text_h0)
    textSizes[2] = adjustText(R.dimen.text_h1)
    textSizes[3] = adjustText(R.dimen.text_h2)
    textSizes[4] = adjustText(R.dimen.text_h3)
    textSizes[5] = adjustText(R.dimen.text_h4)
    textSizes[6] = adjustText(R.dimen.text_h5)
    textSizes[7] = adjustText(R.dimen.text_h6)
  }
  
  private fun adjustText(textResId: Int): Float {
    return adjustToScreenSize(adjustTextToDpi(textResId))
  }
  
  private fun adjustToScreenSize(textSize: Float): Float {
    return when (ContextHolder.context.resources.getInteger(R.integer.screen_type)) {
      0 -> textSize
      1 -> textSize * 1.1f
      2 -> textSize * 1.4f
      3 -> textSize * 1.8f
      else -> throw IllegalStateException()
    }
  }
  
  private fun adjustTextToDpi(textResId: Int): Float {
    val textSize = dimen(textResId)
    val densityDpi = ContextHolder.context.resources.displayMetrics.densityDpi
    return when {
      densityDpi <= 120 -> textSize * 0.75f
      densityDpi <= 160 -> textSize * 0.80f
      densityDpi <= 240 -> textSize * 0.85f
      densityDpi <= 320 -> textSize * 0.9f
      densityDpi <= 480 -> textSize * 1.0f
      densityDpi <= 640 -> textSize * 1.1f
      else -> textSize
    }
  }
}