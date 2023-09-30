package com.arsvechkarev.vault.core.views.drawables

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Animatable
import android.os.SystemClock
import android.view.View
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.viewbuilding.Colors

abstract class BaseShimmerDrawable(
  private val shinePrimaryColor: Int = Colors.Shine,
  private val shineSecondaryColor: Int = Colors.Transparent,
  private val durationMillis: Long = Durations.Shimmer
) : BaseDrawable(), Animatable, Runnable {
  
  private val shinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var isRunning = false
  private var shineXOffset = 0f
  private var shineDx = 0f
  private val shineRect = RectF()
  private val path = Path()
  
  private var shineXStart = 0f
  private var shineXEnd = 0f
  
  abstract fun initBackgroundPath(path: Path, width: Float, height: Float)
  
  open fun onDrawBackground(canvas: Canvas) = Unit
  
  override fun onBoundsChange(bounds: Rect) {
    val width = bounds.width().toFloat()
    val height = bounds.height().toFloat()
    val shineWidth = width * 3
    shineXStart = -width * 3
    shineXEnd = width * 4
    shineDx = width * 7 / (durationMillis.toFloat() / 1000 * 60)
    shineRect.set(shineXStart, -height, shineXStart + shineWidth, height * 2f)
    val gradient = LinearGradient(
      shineRect.left,
      shineRect.height() / 2f,
      shineRect.right,
      shineRect.height() / 2f,
      intArrayOf(shineSecondaryColor, shinePrimaryColor,
        shineSecondaryColor),
      floatArrayOf(0f, 0.5f, 1f),
      Shader.TileMode.REPEAT
    )
    shinePaint.shader = gradient
    path.reset()
    initBackgroundPath(path, width, height)
  }
  
  override fun draw(canvas: Canvas) {
    onDrawBackground(canvas)
    val count = canvas.save()
    canvas.apply {
      clipPath(path)
      rotate(20f, width / 2f, height / 2f)
      translate(shineXOffset, 0f)
      drawRect(shineRect, shinePaint)
    }
    canvas.restoreToCount(count)
  }
  
  override fun start() {
    if (isRunning) return
    isRunning = true
    shineXOffset = shineXStart
    scheduleSelf(this, SystemClock.uptimeMillis() + 1000 / 60)
  }
  
  override fun stop() {
    if (!isRunning) return
    isRunning = false
    unscheduleSelf(this)
  }
  
  override fun isRunning(): Boolean {
    return isRunning
  }
  
  override fun run() {
    if (!isRunning) {
      return
    }
    shineXOffset += shineDx
    if (shineXOffset >= shineXEnd) {
      shineXOffset = shineXStart
    }
    scheduleSelf(this, SystemClock.uptimeMillis() + 1000 / 60)
    invalidateSelf()
  }
  
  companion object {
    
    fun View.stopShimmerDrawable() {
      (background as? BaseShimmerDrawable)?.apply { stop() }
    }
    
    fun View.setShimmerDrawable(drawable: BaseShimmerDrawable) {
      background = drawable.apply { post { start() } }
    }
  }
}
