package com.arsvechkarev.vault.core.views.drawables

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
  textColor: Int = Colors.AccentHeavy,
  backgroundColor: Int = Colors.WhiteCircle
) : BaseDrawable() {
  
  private val textPaint = TextPaint(color = textColor, font = Fonts.SegoeUiBold)
  private val paint = Paint(color = backgroundColor)
  private var halfTextHeight = 0f
  
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
    halfTextHeight = textPaint.getTextHeight(letter) / 2f
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
      val textHeight = textPaint.getTextHeight(letter)
      if (textHeight > bounds.height() * 0.4f) {
        break
      }
      textPaint.textSize++
    }
  }
}
