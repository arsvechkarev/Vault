package com.arsvechkarev.vault.core.views

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.view.View
import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.core.extensions.Paint
import com.arsvechkarev.vault.features.common.DurationsConfigurator
import com.arsvechkarev.vault.viewbuilding.Colors
import viewdsl.AccelerateDecelerateInterpolator

class PasswordStrengthMeter(context: Context) : View(context) {
  
  private var strength: PasswordStrength? = null
  private var percentage = 0f
  private val paint = Paint(Colors.Transparent)
  private val colorAnimator = ValueAnimator().apply {
    duration = DurationsConfigurator.Default
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      paint.color = it.animatedValue as Int
    }
  }
  private val percentageAnimator = ValueAnimator().apply {
    duration = DurationsConfigurator.Default
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      percentage = it.animatedValue as Float
      invalidate()
    }
  }
  
  fun setStrength(strength: PasswordStrength?, animate: Boolean = true) {
    if (this.strength == strength) return
    this.strength = strength
    val resultPair = when (strength) {
      PasswordStrength.WEAK -> Pair(0.25f, Colors.PasswordWeak)
      PasswordStrength.MEDIUM -> Pair(0.50f, Colors.PasswordMedium)
      PasswordStrength.STRONG -> Pair(0.75f, Colors.PasswordStrong)
      PasswordStrength.VERY_STRONG -> Pair(1f, Colors.PasswordVeryStrong)
      null -> Pair(0f, Colors.Transparent)
    }
    if (animate) {
      percentageAnimator.setFloatValues(percentage, resultPair.first)
      percentageAnimator.start()
      colorAnimator.setIntValues(paint.color, resultPair.second)
      colorAnimator.setEvaluator(ArgbEvaluator())
      colorAnimator.start()
    } else {
      percentage = resultPair.first
      paint.color = resultPair.second
      invalidate()
    }
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawRoundRect(
      0f, 0f, width * percentage, height.toFloat(), height / 2f, height / 2f, paint
    )
  }
}
