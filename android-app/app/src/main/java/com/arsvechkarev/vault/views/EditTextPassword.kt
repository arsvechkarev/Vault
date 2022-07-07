package com.arsvechkarev.vault.views

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.image
import viewdsl.layoutLeftTop
import viewdsl.marginEnd
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onSubmit
import viewdsl.onTextChanged
import viewdsl.paddings
import viewdsl.withViewBuilder

class EditTextPassword(context: Context) : FrameLayout(context) {
  
  private val editText get() = getChildAt(0) as EditText
  private val passwordIcon get() = getChildAt(1) as ImageView
  
  private var isPasswordHidden = true
  
  init {
    withViewBuilder {
      val imageSize = IconSize
      val iconMarginStart = MarginLarge
      EditText(MatchParent, MatchParent, style = BaseEditText) {
        paddings(end = imageSize + iconMarginStart)
        transformationMethod = PasswordTransformationMethod.getInstance()
      }
      ImageView(WrapContent, WrapContent) {
        margins(start = iconMarginStart, top = MarginSmall, bottom = MarginSmall, end = MarginSmall)
        image(R.drawable.ic_eye_closed)
        onClick {
          if (isPasswordHidden) {
            image(R.drawable.ic_eye_opened)
            val selectionStart = editText.selectionStart
            val selectionEnd = editText.selectionEnd
            editText.transformationMethod = null
            editText.setSelection(selectionStart, selectionEnd)
          } else {
            image(R.drawable.ic_eye_closed)
            val selectionStart = editText.selectionStart
            val selectionEnd = editText.selectionEnd
            editText.transformationMethod = PasswordTransformationMethod.getInstance()
            editText.setSelection(selectionStart, selectionEnd)
          }
          isPasswordHidden = !isPasswordHidden
        }
      }
    }
  }
  
  fun setHint(hintResId: Int) = editText.setHint(hintResId)
  
  fun onSubmit(block: (String) -> Unit) = editText.onSubmit(block)
  
  fun onTextChanged(block: (String) -> Unit) = editText.onTextChanged(block)
  
  fun requestEditTextFocus() = editText.requestFocus()
  
  fun text(text: String) = editText.setText(text)
  
  fun text(textResId: Int) = editText.setText(textResId)
  
  fun getText() = editText.text.toString()
  
  fun addTextChangedListener(textWatcher: BaseTextWatcher) {
    editText.addTextChangedListener(textWatcher)
  }
  
  fun removeTextChangedListener(textWatcher: BaseTextWatcher) {
    editText.removeTextChangedListener(textWatcher)
  }
  
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    editText.layoutLeftTop(paddingStart, paddingTop)
    passwordIcon.layoutLeftTop(
      width - passwordIcon.measuredWidth - passwordIcon.marginEnd, paddingTop)
  }
}