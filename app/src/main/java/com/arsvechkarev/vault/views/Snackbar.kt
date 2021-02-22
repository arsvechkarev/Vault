package com.arsvechkarev.vault.views

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.vault.core.extensions.i
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.CheckmarkSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.vault.viewdsl.DURATION_SHORT
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.atMost
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.exactly
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutLeftTop
import com.arsvechkarev.vault.viewdsl.paddings
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.AnimatableCheckmark.Companion.CHECKMARK_DELAY

class Snackbar(context: Context) : ViewGroup(context) {
  
  private val innerPadding = Dimens.MarginSmall
  private var opened = false
  
  val textInfo get() = getChildAt(0) as TextView
  val checkmarkView get() = getChildAt(1) as AnimatableCheckmark
  
  val isOpened get() = opened
  
  init {
    clipToPadding = false
    backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
    paddings(start = MarginDefault, end = MarginDefault, top = MarginSmall, bottom = MarginSmall)
    addView(TextView(context).apply(BaseTextView).apply {
      textSize(TextSizes.H4)
    })
    addView(AnimatableCheckmark(context).apply {
      val padding = MarginSmall
      setPadding(padding, padding, padding, padding)
    })
  }
  
  fun show(textResId: Int) {
    if (opened) return
    textInfo.text(textResId)
    opened = true
    checkmarkView.animateVisible()
    checkmarkView.animateCheckmark()
    animate().translationY(0f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(DURATION_SHORT)
        .withLayer()
        .start()
    postDelayed({ hide() }, DURATION_SHORT + CHECKMARK_DELAY + SHOWING_TIME)
  }
  
  fun hide() {
    if (!opened) return
    opened = false
    animate().translationY(height * 1.8f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(DURATION_SHORT)
        .withLayer()
        .withEndAction { checkmarkView.invisible() }
        .start()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = widthMeasureSpec.size
    val checkmarkHeight = CheckmarkSize
    val checkmarkWidth = exactly((checkmarkHeight * 1.3333f).i)
    checkmarkView.measure(checkmarkWidth, exactly(checkmarkHeight))
    val textInfoWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        checkmarkView.measuredWidth
    textInfo.measure(atMost(textInfoWidth), heightMeasureSpec)
    val height = maxOf(textInfo.measuredHeight, checkmarkView.height) + paddingTop + paddingBottom
    setMeasuredDimension(width, resolveSize(height, heightMeasureSpec))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    checkmarkView.layoutLeftTop(paddingStart, height / 2 - checkmarkView.measuredHeight / 2)
    val textInfoTop = height / 2 - textInfo.measuredHeight / 2
    textInfo.layoutLeftTop(checkmarkView.right + innerPadding * 2, textInfoTop)
    if (!isOpened) {
      translationY = height * 1.5f
    }
  }
  
  companion object {
    
    const val SHOWING_TIME = 1000
  }
}