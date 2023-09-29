package com.arsvechkarev.vault.core.views

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.CheckmarkSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.animateVisible
import viewdsl.atMost
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.exactly
import viewdsl.invisible
import viewdsl.layoutLeftTop
import viewdsl.paddings
import viewdsl.size
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class Snackbar(context: Context) : ViewGroup(context) {
  
  private val innerPadding = MarginSmall
  private var opened = false
  
  val textInfo get() = getChildAt(0) as TextView
  val checkmarkView get() = getChildAt(1) as AnimatableCheckmark
  
  val isOpened get() = opened
  
  init {
    clipToPadding = false
    backgroundRoundRect(Dimens.CornerRadiusDefault, Colors.Snackbar)
    paddings(start = MarginNormal, end = MarginNormal, top = MarginSmall, bottom = MarginSmall)
    addView(TextView(context).apply(BaseTextView).apply {
      textSize(TextSizes.H6)
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
        .setDuration(Durations.Short)
        .withLayer()
        .start()
    val delay = Durations.Short + Durations.Checkmark +
        Durations.Snackbar
    postDelayed({ hide() }, delay)
  }
  
  fun hide() {
    if (!opened) return
    opened = false
    animate().translationY(height * 1.8f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(Durations.Short)
        .withLayer()
        .withEndAction { checkmarkView.invisible() }
        .start()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = widthMeasureSpec.size
    val checkmarkHeight = CheckmarkSize
    val checkmarkWidth = exactly((checkmarkHeight * 1.3333f).toInt())
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
    
    val BaseFragmentScreen.snackbar get() = viewAs<Snackbar>()
    
    fun ViewGroup.Snackbar(block: Snackbar.() -> Unit) = withViewBuilder {
      val snackbar = Snackbar(context)
      snackbar.size(MatchParent, WrapContent)
      snackbar.classNameTag()
      addView(snackbar)
      snackbar.apply(block)
    }
  }
}
