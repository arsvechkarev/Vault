package com.arsvechkarev.vault.viewbuilding

import android.graphics.Color

@Suppress("MayBeConstant")
object Colors {
  
  val Transparent = Color.TRANSPARENT
  val Black = "000000".color
  val Background = "17212B".color
  val Dialog = "17212B".color
  val Icon = "FFFFFF".color
  val Ripple = "C6FFFFFF".color
  val Accent = "3C80AE".color
  val AccentLight = "69ACD8".color
  val AccentRipple = "70307FB5".color
  val AccentHeavy = "235575".color
  val AccentDisabled = "436074".color
  val WhiteCircle = "FFFFFF".color
  val Correct = "29B500".color
  val Error = "D5301E".color
  val ErrorRipple = "70E64545".color
  val TextPrimary = "FFFFFF".color
  val TextSecondary = "A3A3A3".color
  val TextDisabled = "616161".color
  val PasswordWeak = "C80505".color
  val PasswordMedium = "DA6314".color
  val PasswordStrong = "DAAF14".color
  val PasswordVeryStrong = "069A15".color
  val Divider = "2B4453".color
  val Shadow = "BB000000".color
  val SwitchThumbUnchecked = "B9B9B9".color
  val SwitchTrackUnchecked = "5D646B".color
  val Shine = "BBD1D1D1".color
  val SnackbarDefault = "20475E".color
  val SnackbarError = Error
  val Favorite = "F4E216".color
  val FavoriteRipple = "C6F4E216".color
  
  private inline val String.color: Int
    get() = Color.parseColor("#$this")
}
