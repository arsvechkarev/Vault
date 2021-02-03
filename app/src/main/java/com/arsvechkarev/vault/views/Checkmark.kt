package com.arsvechkarev.vault.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import androidx.core.content.ContextCompat
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.TEMP_RECT
import com.arsvechkarev.vault.core.extensions.i
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.vault.viewdsl.DURATION_SHORT

class Checkmark(context: Context) : View(context) {
  
  private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Colors.Correct
    style = Paint.Style.STROKE
  }
  
  private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    color = Colors.TextPrimary
    style = Paint.Style.STROKE
  }
  
  private val checkmarkAppear = ContextCompat.getDrawable(context,
    R.drawable.avd_chechmark_appear) as AnimatedVectorDrawable
  
  private val checkmarkDisappear = ContextCompat.getDrawable(context,
    R.drawable.avd_chechmark_disappear) as AnimatedVectorDrawable
  
  private var currentDrawable = checkmarkDisappear
  private var _isChecked = false
  private var _drawBorder = false
  
  private val animator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      circlePaint.strokeWidth = it.animatedValue as Float
      invalidate()
    }
  }
  
  private val borderAnimator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      borderPaint.strokeWidth = it.animatedValue as Float
      invalidate()
    }
  }
  
  var drawBorder: Boolean
    get() = _drawBorder
    set(value) {
      _drawBorder = value
      updateBorderState()
    }
  
  var isChecked: Boolean
    get() = _isChecked
    set(value) {
      if (_isChecked == value) return
      _isChecked = value
      updateCheckedState()
    }
  
  init {
    val colorFilter = PorterDuffColorFilter(Colors.Icon, PorterDuff.Mode.SRC_ATOP)
    checkmarkAppear.colorFilter = colorFilter
    checkmarkDisappear.colorFilter = colorFilter
  }
  
  fun updateCheckedStateWithoutAnimation(isChecked: Boolean) {
    _isChecked = isChecked
    if (_isChecked) {
      circlePaint.strokeWidth = getCircleStrokeWidth()
      currentDrawable = checkmarkAppear
    } else {
      circlePaint.strokeWidth = 0f
      currentDrawable = checkmarkDisappear
    }
    invalidate()
  }
  
  fun updateBorderStateWithoutAnimation(drawBorder: Boolean) {
    _drawBorder = drawBorder
    if (_drawBorder) {
      borderPaint.strokeWidth = getBorderWidth()
    } else {
      borderPaint.strokeWidth = 0f
    }
    invalidate()
  }
  
  override fun onDraw(canvas: Canvas) {
    if (checkmarkAppear.bounds.width() == 0) {
      setCheckmarkBounds(TEMP_RECT)
      checkmarkAppear.bounds = TEMP_RECT
    }
    if (checkmarkDisappear.bounds.width() == 0) {
      setCheckmarkBounds(TEMP_RECT)
      checkmarkDisappear.bounds = TEMP_RECT
    }
    if (borderPaint.strokeWidth > 0f) {
      val border = borderPaint.strokeWidth / 2f
      canvas.drawCircle(
        width / 2f, height / 2f,
        width / 2f - border * 1.3f, borderPaint
      )
    }
    if (circlePaint.strokeWidth > 0f) {
      canvas.drawCircle(width / 2f, height / 2f,
        maxOf(width, height) / 2 - circlePaint.strokeWidth / 2, circlePaint)
      currentDrawable.draw(canvas)
    }
  }
  
  private fun updateCheckedState() {
    if (_isChecked) {
      currentDrawable = checkmarkAppear
      animator.setFloatValues(circlePaint.strokeWidth, getCircleStrokeWidth())
    } else {
      currentDrawable = checkmarkDisappear
      animator.setFloatValues(circlePaint.strokeWidth, 0f)
    }
    currentDrawable.start()
    animator.start()
  }
  
  private fun updateBorderState() {
    if (_drawBorder) {
      borderAnimator.setFloatValues(borderPaint.strokeWidth, getBorderWidth())
    } else {
      borderAnimator.setFloatValues(borderPaint.strokeWidth, 0f)
    }
    borderAnimator.start()
  }
  
  private fun setCheckmarkBounds(rect: Rect) {
    rect.set(
      (width / 2f - width * 0.4f).i,
      (height / 2f - height * 0.4f).i,
      (width / 2f + width * 0.4f).i,
      (height / 2f + height * 0.4f).i
    )
  }
  
  private fun getBorderWidth(): Float {
    val w = layoutParams.width
    val h = layoutParams.height
    return maxOf(h, w) / 13f
  }
  
  private fun getCircleStrokeWidth(): Float {
    val w = layoutParams.width
    val h = layoutParams.height
    return maxOf(h, w) / 2f
  }
}