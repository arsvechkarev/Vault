package com.arsvechkarev.vault.views

import android.content.Context
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.arsvechkarev.vault.viewbuilding.Dimens.CheckmarkSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import viewdsl.Size.Companion.WrapContent
import viewdsl.gravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.orientation
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.text
import viewdsl.withViewBuilder

class CheckmarkAndTextViewGroup(
  context: Context,
  private val text: CharSequence
) : LinearLayout(context) {
  
  private val checkmark get() = getChildAt(0) as Checkmark
  
  var isChecked: Boolean
    get() = checkmark.isChecked
    set(value) {
      checkmark.isChecked = value
    }
  
  init {
    orientation(HORIZONTAL)
    withViewBuilder {
      paddingHorizontal(MarginNormal)
      paddingVertical(MarginTiny)
      gravity(CENTER_VERTICAL)
      onClick {
        val checkmark = getChildAt(0) as Checkmark
        checkmark.isChecked = !checkmark.isChecked
      }
      child<Checkmark>(CheckmarkSize, CheckmarkSize) {
        margins(end = MarginSmall)
        drawBorder = true
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        margin(MarginSmall)
        text(this@CheckmarkAndTextViewGroup.text)
      }
    }
  }
  
  companion object {
    
    fun ViewGroup.CheckmarkAndTextViewGroup(textResId: Int, block: CheckmarkAndTextViewGroup.() -> Unit) {
      val text = context.getString(textResId)
      val view = CheckmarkAndTextViewGroup(context, text)
      view.id = textResId
      view.apply(block)
      addView(view, ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    }
  }
}