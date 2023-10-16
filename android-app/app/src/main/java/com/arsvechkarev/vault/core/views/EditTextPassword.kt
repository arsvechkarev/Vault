package com.arsvechkarev.vault.core.views

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.TextSizes
import domain.Password
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.font
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onSubmit
import viewdsl.onTextChanged
import viewdsl.paddings
import viewdsl.showKeyboard
import viewdsl.textSize
import viewdsl.withViewBuilder

class EditTextPassword(context: Context) : FrameLayout(context) {
  
  val editText get() = getChildAt(0) as EditText
  
  var isPasswordHidden = true
    private set
  
  init {
    withViewBuilder {
      val imageSize = ImageSize
      val iconMarginStart = MarginLarge
      EditText(MatchParent, MatchParent) {
        paddings(top = MarginSmall, bottom = MarginSmall, end = imageSize + iconMarginStart)
        font(Fonts.SegoeUi)
        textSize(TextSizes.H4)
        setSingleLine()
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        imeOptions = EditorInfo.IME_ACTION_DONE
        transformationMethod = PasswordTransformationMethod.getInstance()
      }
      ImageView(WrapContent, WrapContent) {
        classNameTag()
        margins(start = iconMarginStart, top = MarginSmall, bottom = MarginSmall, end = MarginSmall)
        layoutGravity(Gravity.END)
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
  
  fun showKeyboard() {
    editText.requestFocus()
    context.showKeyboard(editText)
  }
  
  fun setHint(hintResId: Int) = editText.setHint(hintResId)
  
  fun onSubmit(block: (Password) -> Unit) = editText.onSubmit {
    block(Password.create(it.toString()))
  }
  
  fun onTextChanged(block: (Password) -> Unit) = editText.onTextChanged {
    block(Password.create(it))
  }
  
  fun requestEditTextFocus() = editText.requestFocus()
  
  fun clearEditTextFocus() = editText.clearFocus()
  
  fun setRawText(text: String) = editText.setText(text)
  
  fun getPassword() = Password.create(editText.text.toString())
  
  fun clearText() {
    editText.clearText()
  }
  
  fun clearTextAndFocus() {
    editText.clearFocus()
    editText.clearText()
  }
}
