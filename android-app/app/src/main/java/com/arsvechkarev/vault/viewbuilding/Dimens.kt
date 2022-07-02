package com.arsvechkarev.vault.viewbuilding

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewdsl.ContextHolder
import com.arsvechkarev.vault.viewdsl.dp
import com.arsvechkarev.vault.viewdsl.screenHeight
import com.arsvechkarev.vault.viewdsl.screenWidth

object Dimens {
  
  private const val SCREEN_TYPE_SMALL = 0
  private const val SCREEN_TYPE_MEDIUM = 1
  private const val SCREEN_TYPE_LARGE = 2
  private const val SCREEN_TYPE_XLARGE = 3
  
  val VerticalMarginSmall get() = adjustVertical(8.dp)
  val HorizontalMarginSmall get() = adjust(8.dp)
  val HorizontalMarginVerySmall get() = adjustHorizontal(4.dp)
  val MarginTiny get() = adjust(4.dp)
  val MarginSmall get() = adjust(8.dp)
  val MarginMedium get() = adjust(12.dp)
  val MarginNormal get() = adjust(16.dp)
  val MarginLarge get() = adjust(24.dp)
  val MarginExtraLarge get() = adjust(32.dp)
  val IconSize get() = adjust(24.dp)
  val ItemServiceInfoImageSize get() = adjust(36.dp)
  val PasswordActionsViewImageSize get() = adjust(36.dp)
  val CheckmarkSize get() = adjust(20.dp)
  val IconPadding get() = adjust(6.dp)
  val ImageLogoSize get() = adjust(90.dp)
  val ImageNoServicesSize get() = adjust(100.dp)
  val ImageServiceNameSize get() = adjust(45.dp)
  val ImageBackMargin get() = adjust(16.dp)
  val CornerRadiusSmall get() = (3.dp)
  val CornerRadiusDefault get() = (6.dp)
  val DividerHeight get() = adjustDivider(1.dp)
  val PasswordStrengthMeterHeight get() = adjust(6.dp)
  val ProgressBarSizeBig get() = adjust(60.dp)
  val FingerprintIconSize get() = adjust(60.dp)
  val FabSize get() = adjust(60.dp)
  val HorizontalMarginPasswordsActionView get() = adjustHorizontal(24.dp)
  
  private fun adjust(size: Int): Int {
    val type = ContextHolder.applicationContext.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size
    if (type == SCREEN_TYPE_MEDIUM) return (size * 1.5f).toInt()
    if (type == SCREEN_TYPE_LARGE) return (size * 1.75f).toInt()
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
  
  private fun adjustVertical(size: Int): Int {
    val height = maxOf(ContextHolder.applicationContext.screenHeight,
      ContextHolder.applicationContext.screenWidth)
    if (height < 1200) return size
    if (height < 1800) return (size * 1.6f).toInt()
    if (height < 2400) return (size * 2f).toInt()
    return (size * 2.5f).toInt()
  }
  
  private fun adjustDivider(size: Int): Int {
    val type = ContextHolder.applicationContext.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size / 2
    if (type == SCREEN_TYPE_MEDIUM) return size
    if (type == SCREEN_TYPE_LARGE) return (size * 1.5f).toInt()
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
  
  private fun adjustHorizontal(size: Int): Int {
    val type = ContextHolder.applicationContext.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size
    if (type == SCREEN_TYPE_MEDIUM) return (size * 1.5f).toInt()
    if (type == SCREEN_TYPE_LARGE) return (size * 2f).toInt()
    if (type == SCREEN_TYPE_XLARGE) return (size * 2.5f).toInt()
    return size
  }
}