package com.arsvechkarev.vault.viewbuilding

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.i
import com.arsvechkarev.vault.core.extensions.screenHeight
import com.arsvechkarev.vault.core.extensions.screenWidth
import com.arsvechkarev.vault.viewdsl.ContextHolder
import com.arsvechkarev.vault.viewdsl.Ints.dp

object Dimens {
  
  private const val SCREEN_TYPE_SMALL = 0
  private const val SCREEN_TYPE_MEDIUM = 1
  private const val SCREEN_TYPE_LARGE = 2
  private const val SCREEN_TYPE_XLARGE = 3
  
  val VerticalMarginSmall get() = adjustVertical(8.dp)
  val HorizontalMarginSmall get() = adjust(8.dp)
  val HorizontalMarginVerySmall get() = adjustHorizontal(4.dp)
  val MarginVerySmall get() = adjust(4.dp)
  val MarginSmall get() = adjust(8.dp)
  val MarginDefault get() = adjust(16.dp)
  val MarginMedium get() = adjust(24.dp)
  val MarginBig get() = adjust(32.dp)
  val IconSize get() = adjust(24.dp)
  val ItemServiceInfoImageSize get() = adjust(36.dp)
  val CheckmarkSize get() = adjust(20.dp)
  val IconPadding get() = adjust(6.dp)
  val ImageLogoSize get() = adjust(70.dp)
  val ImageServiceNameSize get() = adjust(45.dp)
  val ImageBackMargin get() = adjust(16.dp)
  val DefaultCornerRadius get() = (6.dp)
  val DividerHeight get() = adjustDivider(1.dp)
  val PasswordStrengthMeterHeight get() = adjust(6.dp)
  val ProgressBarSizeBig get() = adjust(60.dp)
  val FabSize get() = adjust(60.dp)
  val HorizontalMarginPasswordsActionView get() = adjustHorizontal(24.dp)
  
  private fun adjust(size: Int): Int {
    val type = ContextHolder.context.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size
    if (type == SCREEN_TYPE_MEDIUM) return (size * 1.5f).i
    if (type == SCREEN_TYPE_LARGE) return (size * 1.75f).i
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
  
  private fun adjustVertical(size: Int): Int {
    val height = maxOf(ContextHolder.context.screenHeight, ContextHolder.context.screenWidth)
    if (height < 1200) return size
    if (height < 1800) return (size * 1.6f).i
    if (height < 2400) return (size * 2f).i
    return (size * 2.5f).i
  }
  
  private fun adjustDivider(size: Int): Int {
    val type = ContextHolder.context.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size / 2
    if (type == SCREEN_TYPE_MEDIUM) return size
    if (type == SCREEN_TYPE_LARGE) return (size * 1.5f).i
    if (type == SCREEN_TYPE_XLARGE) return size * 2
    return size
  }
  
  private fun adjustHorizontal(size: Int): Int {
    val type = ContextHolder.context.resources.getInteger(R.integer.screen_type)
    if (type == SCREEN_TYPE_SMALL) return size
    if (type == SCREEN_TYPE_MEDIUM) return (size * 1.5f).i
    if (type == SCREEN_TYPE_LARGE) return (size * 2f).i
    if (type == SCREEN_TYPE_XLARGE) return (size * 2.5f).i
    return size
    
  }
}