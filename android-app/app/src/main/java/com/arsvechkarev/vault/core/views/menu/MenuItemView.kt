package com.arsvechkarev.vault.core.views.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import com.arsvechkarev.vault.core.extensions.Paint
import com.arsvechkarev.vault.core.extensions.TextPaint
import com.arsvechkarev.vault.core.extensions.getTextHeight
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Fonts
import viewdsl.dp
import viewdsl.retrieveDrawable
import viewdsl.rippleBackground

class MenuItemView(
  context: Context,
  val iconRes: Int,
  val text: String,
  textSize: Float,
  private val circleSize: Int,
) : View(context) {
  
  private val icon = context.retrieveDrawable(iconRes)
  private val textPaint = TextPaint(textSize, font = Fonts.SegoeUi)
  private val circlePaint = Paint(Colors.Accent)
  
  private var firstWord: String? = null
  private var secondWord: String? = null
  
  init {
    rippleBackground(Colors.Ripple, Colors.Transparent, Dimens.CornerRadiusDefault)
    val padding = 6.dp
    setPadding(padding, padding, padding, padding)
    if (text.contains(' ')) {
      firstWord = text.substringBefore(' ')
      secondWord = text.substringAfter(' ')
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val textWidth: Int
    val textHeight: Int
    if (firstWord != null && secondWord != null) {
      val firstWidth = textPaint.measureText(firstWord!!)
      val secondWidth = textPaint.measureText(secondWord!!)
      textWidth = maxOf(firstWidth, secondWidth).toInt()
      textHeight = textPaint.getTextHeight(firstWord!!) +
          textPaint.getTextHeight(secondWord!!) + (getTextPadding() * 2.5f).toInt()
    } else {
      textWidth = textPaint.measureText(text).toInt()
      textHeight = textPaint.getTextHeight() + (getTextPadding() * 1.5f).toInt()
    }
    val width = maxOf(circleSize, textWidth) + paddingStart + paddingEnd
    val height = circleSize + textHeight + paddingTop + paddingBottom
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec),
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    icon.colorFilter = PorterDuffColorFilter(Colors.Icon, PorterDuff.Mode.SRC_ATOP)
    val padding = circleSize / 4
    icon.setBounds(
      w / 2 - circleSize / 2 + padding,
      padding,
      w / 2 + circleSize / 2 - padding,
      circleSize - padding
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, circleSize / 2f, circleSize / 2f - paddingTop, circlePaint)
    icon.draw(canvas)
    if (firstWord != null && secondWord != null) {
      var y = circleSize + getTextPadding().toFloat() + textPaint.getTextHeight(firstWord!!)
      canvas.drawText(firstWord!!, width / 2f, y, textPaint)
      y += textPaint.getTextHeight(firstWord!!) + getTextPadding()
      canvas.drawText(secondWord!!, width / 2f, y, textPaint)
    } else {
      val y = circleSize + getTextPadding().toFloat() + textPaint.getTextHeight()
      canvas.drawText(text, width / 2f, y, textPaint)
    }
  }
  
  private fun getTextPadding(): Int {
    if (firstWord != null && secondWord != null) {
      return (textPaint.getTextHeight() * 0.8f).toInt()
    }
    return (textPaint.getTextHeight())
  }
}
