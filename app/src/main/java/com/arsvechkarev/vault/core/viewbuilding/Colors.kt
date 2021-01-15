package com.arsvechkarev.vault.core.viewbuilding

import android.content.Context
import android.graphics.Color
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.viewdsl.getAttrColor

object Colors {
  
  const val Transparent = Color.TRANSPARENT
  
  private val colors = IntArray(27) { 0 }
  
  val Background get() = colors[0]
  val Surface get() = colors[1]
  val Dialog get() = colors[2]
  val Icon get() = colors[3]
  val Ripple get() = colors[4]
  val Accent get() = colors[5]
  val AccentLight get() = colors[6]
  val OnAccent get() = colors[7]
  val AccentRipple get() = colors[8]
  val Correct get() = colors[9]
  val CorrectRipple get() = colors[10]
  val Error get() = colors[11]
  val ErrorRipple get() = colors[12]
  val TextPrimary get() = colors[13]
  val TextSecondary get() = colors[14]
  val PasswordWeak get() = colors[15]
  val PasswordMedium get() = colors[16]
  val PasswordStrong get() = colors[17]
  val PasswordVeryStrong get() = colors[18]
  val Disabled get() = colors[21]
  val Divider get() = colors[22]
  val Shadow get() = colors[23]
  
  fun init(context: Context) {
    colors[0] = context.getAttrColor(R.attr.colorBackground)
    colors[1] = context.getAttrColor(R.attr.colorSurface)
    colors[2] = context.getAttrColor(R.attr.colorDialog)
    colors[3] = context.getAttrColor(R.attr.colorIcon)
    colors[4] = context.getAttrColor(R.attr.colorRipple)
    colors[5] = context.getAttrColor(R.attr.colorAccent)
    colors[6] = context.getAttrColor(R.attr.colorAccentLight)
    colors[7] = context.getAttrColor(R.attr.colorOnAccent)
    colors[8] = context.getAttrColor(R.attr.colorAccentRipple)
    colors[9] = context.getAttrColor(R.attr.colorCorrect)
    colors[10] = context.getAttrColor(R.attr.colorCorrectRipple)
    colors[11] = context.getAttrColor(R.attr.colorError)
    colors[12] = context.getAttrColor(R.attr.colorErrorRipple)
    colors[13] = context.getAttrColor(R.attr.colorTextPrimary)
    colors[14] = context.getAttrColor(R.attr.colorTextSecondary)
    colors[15] = context.getAttrColor(R.attr.colorPasswordWeak)
    colors[16] = context.getAttrColor(R.attr.colorPasswordMedium)
    colors[17] = context.getAttrColor(R.attr.colorPasswordStrong)
    colors[18] = context.getAttrColor(R.attr.colorPasswordVeryStrong)
    colors[21] = context.getAttrColor(R.attr.colorDisabled)
    colors[22] = context.getAttrColor(R.attr.colorDivider)
    colors[23] = context.getAttrColor(R.attr.colorShadow)
  }
}