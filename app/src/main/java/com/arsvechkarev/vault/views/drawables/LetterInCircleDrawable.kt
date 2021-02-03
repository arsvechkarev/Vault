package com.arsvechkarev.vault.views.drawables

import android.graphics.Canvas
import android.graphics.Rect
import com.arsvechkarev.vault.core.extensions.Paint
import com.arsvechkarev.vault.core.extensions.TextPaint
import com.arsvechkarev.vault.core.extensions.getTextHeight
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.TextSizes

class LetterInCircleDrawable(
  private var letter: String,
  textColor: Int = Colors.TextPrimary,
  backgroundColor: Int = Colors.Accent
) : BaseDrawable() {
  
  private val textPaint = TextPaint(color = textColor, font = Fonts.SegoeUiBold)
  private val paint = Paint(color = backgroundColor)
  private var halfTextHeight = 0f
  
  fun setLetter(letter: String) {
    this.letter = letter
    invalidateSelf()
  }
  
  override fun onBoundsChange(bounds: Rect) {
    computeTextSize()
    halfTextHeight = textPaint.getTextHeight() / 2f
  }
  
  override fun draw(canvas: Canvas) {
    val hw = bounds.width() / 2f
    val hh = bounds.height() / 2f
    canvas.drawCircle(hw, hh, minOf(hw, hh), paint)
    canvas.drawText(letter, hw, hh + halfTextHeight - textPaint.descent() / 2, textPaint)
  }
  
  private fun computeTextSize() {
    textPaint.textSize = TextSizes.H4
    while (true) {
      val textHeight = textPaint.getTextHeight()
      if (textHeight > bounds.height() * 0.3f) {
        break
      }
      textPaint.textSize++
    }
  }
}