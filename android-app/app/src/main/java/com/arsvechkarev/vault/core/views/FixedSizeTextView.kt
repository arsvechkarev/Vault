package com.arsvechkarev.vault.core.views

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import com.arsvechkarev.vault.R

class FixedSizeTextView(context: Context) : AppCompatTextView(context) {
  
  var exampleTextRes = R.string.text_empty
  
  fun measureCurrentTextWidth(): Int {
    return paint.measureText(text.toString()).toInt()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    // Sometimes, when the text is empty, textView measures its height less than it would with
    // non-empty text, which makes layout jerk. To fix it, we are setting non-empty text to the
    // textView before measuring, and then setting the initial text back.
    val textBefore = text
    text = context.getText(exampleTextRes)
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    text = textBefore
  }
}
