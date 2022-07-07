package com.arsvechkarev.vault.viewbuilding

import viewdsl.Densities.densityDpi
import viewdsl.sp

object TextSizes {
  
  val MainHeader: Float get() = 40.sp
  val PasswordStub: Float get() = 36.sp
  val H0: Float get() = 30.sp
  val H1: Float get() = 24.sp
  val H2: Float get() = 22.sp
  val H3: Float get() = 20.sp
  val H4: Float get() = 18.sp
  val H5: Float get() = adjustTextToDpi(16.sp)
  val H6: Float get() = adjustTextToDpi(14.sp)
  
  private fun adjustTextToDpi(textSize: Float): Float {
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