package com.arsvechkarev.vault.core.views.drawables

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import com.arsvechkarev.vault.core.extensions.Paint
import com.arsvechkarev.vault.core.extensions.TextPaint
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Fonts

class LoadingPlaceholderDrawable(
  private val letter: String,
  textColor: Int = Colors.AccentHeavy,
  backgroundColor: Int = Colors.WhiteCircle
) : BaseShimmerDrawable(backgroundColor) {
  
  
  private val circlePaint = Paint(color = backgroundColor)
  private val textPaint = TextPaint(
    color = textColor,
    font = Fonts.SegoeUiBold,
    textAlign = android.graphics.Paint.Align.LEFT
  )
  private var textRect = Rect()
  
  init {
    require(letter.length == 1)
  }
  
  override fun initBackgroundPath(path: Path, width: Float, height: Float) {
    val hw = width / 2f
    val hh = height / 2f
    path.addCircle(hw, hh, minOf(hw, hh), Path.Direction.CW)
  }
  
  override fun onDrawBackground(canvas: Canvas) {
    val hw = bounds.width() / 2f
    val hh = bounds.height() / 2f
    textPaint.textSize = minOf(hw, hh)
    textPaint.getTextBounds(letter, 0, letter.length, textRect)
    canvas.drawCircle(hw, hh, minOf(hw, hh), circlePaint)
    val x = hw - textRect.width() / 2f - textRect.left
    val y = hh + textRect.height() / 2f - textRect.bottom
    canvas.drawText(letter, x, y, textPaint)
  }
}
