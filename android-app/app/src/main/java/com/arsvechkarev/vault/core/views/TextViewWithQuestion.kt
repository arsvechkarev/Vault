package com.arsvechkarev.vault.core.views

import android.content.Context
import android.widget.FrameLayout
import android.widget.ImageView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import viewdsl.Size.Companion.WrapContent
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutLeftTop
import viewdsl.textColor
import viewdsl.unspecified
import viewdsl.visible
import viewdsl.withViewBuilder

class TextWithQuestion(context: Context) : FrameLayout(context) {
  
  private val textView get() = getChildAt(0) as FixedSizeTextView
  private val imageView get() = getChildAt(1) as ImageView
  
  private var textViewMinHeight = 0
  private var onClickListener = {}
  
  init {
    withViewBuilder {
      child<FixedSizeTextView>(WrapContent, WrapContent, style = BaseTextView) {
        exampleTextRes = R.string.text_password_min_length
        textColor(Colors.Error)
      }
      ImageView(WrapContent, WrapContent) {
        image(R.drawable.ic_question, Colors.Error)
        invisible()
      }
    }
  }
  
  fun onClick(block: () -> Unit) {
    onClickListener = block
  }
  
  fun setText(text: String) {
    textView.text = text
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
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    textView.exampleTextRes = R.string.text_password_is_too_weak
    textView.measure(widthMeasureSpec, heightMeasureSpec)
    textViewMinHeight = textView.measuredHeight
    textView.exampleTextRes = R.string.text_password_min_length
    textView.measure(widthMeasureSpec, heightMeasureSpec)
    imageView.measure(unspecified(), unspecified())
    val heightSize = textView.measuredHeight + (imageView.measuredHeight - textViewMinHeight) / 2
    setMeasuredDimension(
      resolveSize(textView.measuredWidth, widthMeasureSpec),
      resolveSize(heightSize, heightMeasureSpec),
    )
  }
  
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    textView.layoutLeftTop(0, (imageView.measuredHeight - textViewMinHeight) / 2)
    imageView.layoutLeftTop(textView.measureCurrentTextWidth() + MarginSmall, 0)
  }
}
