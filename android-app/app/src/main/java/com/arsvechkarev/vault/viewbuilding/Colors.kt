package com.arsvechkarev.vault.viewbuilding

import android.graphics.Color

@Suppress("MayBeConstant")
object Colors {
  
  val Transparent = Color.TRANSPARENT
  val Background = "17212B".color
  val Surface = "383838".color
  val Dialog = "17212B".color
  val Icon = "FFFFFF".color
  val Ripple = "C6FFFFFF".color
  val Accent = "3C80AE".color
  val AccentLight = "69ACD8".color
  val AccentRipple = "70307FB5".color
  val Correct = "29B500".color
  val CorrectRipple = "70249802".color
  val Error = "D5301E".color
  val ErrorRipple = "70E64545".color
  val TextPrimary = "FFFFFF".color
  val TextSecondary = "A3A3A3".color
  val PasswordWeak = "C80505".color
  val PasswordMedium = "DA6314".color
  val PasswordStrong = "DAAF14".color
  val PasswordVeryStrong = "069A15".color
  val Disabled = "AFAFAF".color
  val Divider = "AFAFAF".color
  val Shadow = "BB000000".color
  
  private inline val String.color: Int
    get() = Color.parseColor("#$this")
}