package com.arsvechkarev.vault.core.views

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import viewdsl.Size.Companion.WrapContent
import viewdsl.image
import viewdsl.invisible
import viewdsl.margins
import viewdsl.orientation
import viewdsl.textColor
import viewdsl.visible
import viewdsl.withViewBuilder

class TextWithQuestion(context: Context) : LinearLayout(context) {
  
  private val textView get() = getChildAt(0) as FixedHeightTextView
  private val imageView get() = getChildAt(1) as ImageView
  
  private var onClickListener = {}
  
  init {
    orientation(HORIZONTAL)
    withViewBuilder {
      child<FixedHeightTextView>(WrapContent, WrapContent, style = BaseTextView) {
        textColor(Colors.Error)
      }
      ImageView(WrapContent, WrapContent) {
        image(R.drawable.ic_question, Colors.Error)
        margins(start = MarginSmall)
        invisible()
      }
    }
  }
  
  fun onClick(block: () -> Unit) {
    onClickListener = block
  }
  
  fun setText(@StringRes textRes: Int) {
    textView.setText(textRes)
  }
  
  fun clear() {
    textView.text = ""
    hideQuestion()
  }
  
  fun showQuestion() {
    imageView.visible()
    setOnClickListener { onClickListener() }
  }
  
  fun hideQuestion() {
    imageView.invisible()
    setOnClickListener(null)
  }
}