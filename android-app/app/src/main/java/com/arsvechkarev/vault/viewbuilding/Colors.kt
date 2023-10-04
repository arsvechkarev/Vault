package com.arsvechkarev.vault.viewbuilding

import android.graphics.Color

@Suppress("MayBeConstant")
object Colors {
  
  val Transparent = Color.TRANSPARENT
  val Background = "17212B".color
  val Snackbar = "20475E".color
  val Dialog = "17212B".color
  val Icon = "FFFFFF".color
  val Ripple = "C6FFFFFF".color
  val Accent = "3C80AE".color
  val AccentLight = "69ACD8".color
  val AccentRipple = "70307FB5".color
  val AccentHeavy = "235575".color
  val WhiteCircle = "FFFFFF".color
  val Correct = "29B500".color
  val Error = "D5301E".color
  val ErrorRipple = "70E64545".color
  val TextPrimary = "FFFFFF".color
  val TextSecondary = "A3A3A3".color
  val PasswordWeak = "C80505".color
  val PasswordMedium = "DA6314".color
  val PasswordStrong = "DAAF14".color
  val PasswordVeryStrong = "069A15".color
  val Divider = "2B4453".color
  val Shadow = "BB000000".color
  val SwitchThumbDisabled = "B9B9B9".color
  val SwitchTrackDisabled = "5D646B".color
  val Shine = "BBD1D1D1".color
  val Favorite = "F4E216".color
  val FavoriteRipple = "C6F4E216".color
  
  private inline val String.color: Int
    get() = Color.parseColor("#$this")
}
