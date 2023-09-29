package com.arsvechkarev.vault.viewbuilding

import com.arsvechkarev.vault.R
import viewdsl.ViewBuilder
import viewdsl.ViewDslConfiguration
import viewdsl.dp
import viewdsl.retrieveDrawable

object Dimens {
  
  private const val SCREEN_TYPE_SMALL = 0
  private const val SCREEN_TYPE_MEDIUM = 1
  private const val SCREEN_TYPE_LARGE = 2
  private const val SCREEN_TYPE_XLARGE = 3
  
  val HorizontalMarginSmall get() = adjust(8.dp)
  val MarginTiny get() = adjust(4.dp)
  val MarginSmall get() = adjust(8.dp)
  val MarginMedium get() = adjust(12.dp)
  val MarginNormal get() = adjust(16.dp)
  val MarginLarge get() = adjust(24.dp)
  val MarginExtraLarge get() = adjust(32.dp)
  val IconSize get() = adjust(24.dp)
  val IconSizeBig get() = adjust(36.dp)
  val CheckmarkSize get() = adjust(20.dp)
  val IconPadding get() = adjust(6.dp)
  val ImageLogoSize get() = adjust(90.dp)
  val ImageNoEntriesSize get() = adjust(100.dp)
  val ImageTitleSize get() = adjust(45.dp)
  val ImageBackMargin get() = adjust(16.dp)
  val CornerRadiusSmall get() = (3.dp)
  val CornerRadiusDefault get() = (6.dp)
  val DividerHeight get() = adjustDivider(2.dp)
  val PasswordStrengthMeterHeight get() = adjust(6.dp)
  val ProgressBarSizeSmall get() = adjust(20.dp)
  val ProgressBarSizeBig get() = adjust(60.dp)
  
  val ViewBuilder.GradientDrawableHeight: Int
    get() {
      val drawable = context.retrieveDrawable(R.drawable.bg_gradient)
      return (drawable.intrinsicHeight / 1.4).toInt()
    }
  
  private fun adjust(size: Int): Int {
    val type = ViewDslConfiguration.applicationContext.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size
    if (type == SCREEN_TYPE_MEDIUM) return (size * 1.5f).toInt()
    if (type == SCREEN_TYPE_LARGE) return (size * 1.75f).toInt()
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
  
  private fun adjustDivider(size: Int): Int {
    val type = ViewDslConfiguration.applicationContext.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size / 2
    if (type == SCREEN_TYPE_MEDIUM) return size
    if (type == SCREEN_TYPE_LARGE) return (size * 1.5f).toInt()
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
}
