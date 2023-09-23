package com.arsvechkarev.vault.core.views

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.arsvechkarev.vault.R

class FixedHeightTextView(context: Context) : AppCompatTextView(context) {
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // Sometimes, when the text is empty, textView measures its height less than it would with
    // non-empty text, which makes layout jerk. To fix it, we are setting non-empty text to the
    // textView before measuring, and then setting the initial text back.
    if (text.isBlank()) {
      val textBefore = text
      text = context.getText(R.string.text_text)
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      val minHeight = measuredHeight
      text = textBefore
      val newHeightSpec = MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY)
      super.onMeasure(widthMeasureSpec, newHeightSpec)
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }
}
