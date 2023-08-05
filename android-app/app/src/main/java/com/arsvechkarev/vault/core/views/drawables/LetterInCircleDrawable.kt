package com.arsvechkarev.vault.core.views.drawables

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.arsvechkarev.vault.core.extensions.Paint
import com.arsvechkarev.vault.core.extensions.TextPaint
import com.arsvechkarev.vault.core.extensions.getTextHeight
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.TextSizes

class LetterInCircleDrawable(
  private var letter: String,
  textColor: Int = Colors.AccentHeavy,
  backgroundColor: Int = Colors.WhiteCircle
) : BaseDrawable() {
  
  private val textPaint = TextPaint(
    color = textColor,
    font = Fonts.SegoeUiBold,
    textAlign = Paint.Align.LEFT
  )
  private val paint = Paint(color = backgroundColor)
  private var textRect = Rect()
  
  init {
    require(letter.length == 1)
  }
  
  fun setLetter(letter: String) {
    require(letter.length == 1)
    this.letter = letter
    invalidateSelf()
  }
  
  override fun onBoundsChange(bounds: Rect) {
    computeTextSize()
    textPaint.getTextBounds(letter, 0, letter.length, textRect)
  }
  
  override fun draw(canvas: Canvas) {
    val hw = bounds.width() / 2f
    val hh = bounds.height() / 2f
    canvas.drawCircle(hw, hh, minOf(hw, hh), paint)
    val x = hw - textRect.width() / 2f - textRect.left
    val y = hh + textRect.height() / 2f - textRect.bottom
    canvas.drawText(letter, x, y, textPaint)
  }
  
  private fun computeTextSize() {
    textPaint.textSize = TextSizes.H4
    while (true) {
      val textHeight = textPaint.getTextHeight(letter)
      if (textHeight > bounds.height() * 0.4f) {
        break
      }
      textPaint.textSize++
    }
  }
}
