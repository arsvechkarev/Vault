package com.arsvechkarev.vault.views

import android.content.Context
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.arsvechkarev.vault.viewbuilding.Dimens.CheckmarkSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.orientation
import com.arsvechkarev.vault.viewdsl.paddingHorizontal
import com.arsvechkarev.vault.viewdsl.paddingVertical
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.withViewBuilder

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
      paddingHorizontal(MarginDefault)
      paddingVertical(MarginVerySmall)
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