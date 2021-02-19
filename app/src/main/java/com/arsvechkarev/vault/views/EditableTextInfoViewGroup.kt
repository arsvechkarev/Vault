package com.arsvechkarev.vault.views

import android.content.Context
import android.view.Gravity.CENTER
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.assertThat
import com.arsvechkarev.vault.core.extensions.hideKeyboard
import com.arsvechkarev.vault.core.extensions.showKeyboard
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.background
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.exactly
import com.arsvechkarev.vault.viewdsl.font
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutLeftTop
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.onSubmit
import com.arsvechkarev.vault.viewdsl.onTextChanged
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.unspecified
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.EditableTextInfoViewGroup.ShowingMode.EDITING
import com.arsvechkarev.vault.views.EditableTextInfoViewGroup.ShowingMode.SHOWING

class EditableTextInfoViewGroup(context: Context) : ViewGroup(context) {
  
  private val editText get() = getChildAt(0) as EditText
  private val textView get() = getChildAt(1) as TextView
  private val imageEdit get() = getChildAt(2) as ImageView
  private val imageSave get() = getChildAt(3) as ImageView
  
  private var mode = SHOWING
  
  var transferTextToEditTextWhenSwitching = true
  var allowSavingWhenEmpty = false
  var onEditClickAllowed: () -> Boolean = { true }
  var onSwitchToEditMode: () -> Unit = {}
  var onTextSaved: (String) -> Unit = {}
  
  init {
    withViewBuilder {
      EditText(MatchParent, WrapContent, style = BaseEditText) {
        invisible()
        background(null)
        gravity(CENTER)
        textSize(TextSizes.H3)
        font(Fonts.SegoeUiBold)
        isSingleLine = false
        onSubmit { changeModeToShowing() }
      }
      TextView(WrapContent, WrapContent, style = BoldTextView) {
        gravity(CENTER)
        textSize(TextSizes.H3)
      }
      ImageView(WrapContent, WrapContent) {
        padding(IconPadding)
        image(R.drawable.ic_edit)
        circleRippleBackground()
        onClick { changeModeIfPossible() }
      }
      ImageView(WrapContent, WrapContent) {
        invisible()
        padding(IconPadding)
        image(R.drawable.ic_checmark)
        circleRippleBackground()
        onClick { changeModeIfPossible() }
      }
    }
  }
  
  fun onEditTextChanged(block: (String) -> Unit) {
    editText.onTextChanged(block)
  }
  
  fun setText(text: CharSequence) {
    editText.text(text)
    textView.text(text)
  }
  
  fun changeModeToEditing() {
    if (mode == EDITING) return
    if (!onEditClickAllowed()) return
    mode = EDITING
    if (transferTextToEditTextWhenSwitching) {
      editText.text(textView.text)
    } else {
      editText.text.clear()
    }
    onSwitchToEditMode()
    editText.setSelection(editText.text.length)
    editText.visible()
    textView.invisible()
    editText.requestFocus()
    context.showKeyboard()
    imageSave.animateVisible()
    imageEdit.animateInvisible()
  }
  
  fun changeModeToShowing() {
    if (mode == SHOWING) return
    val text = editText.text.toString().trim()
    if (editText.text.isBlank() && !allowSavingWhenEmpty) return
    onTextSaved(text)
    mode = SHOWING
    editText.invisible()
    textView.visible()
    editText.clearFocus()
    context.hideKeyboard(editText)
    imageSave.animateInvisible()
    imageEdit.animateVisible()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSize = widthMeasureSpec.size
    val maxTextWidth = widthSize - IconSize * 2 - MarginSmall * 2 - MarginDefault
    imageEdit.measure(unspecified(), unspecified())
    imageSave.measure(unspecified(), unspecified())
    textView.measure(exactly(maxTextWidth), unspecified())
    editText.measure(exactly(maxTextWidth), unspecified())
    val imageSize = maxOf(imageEdit.measuredHeight, imageSave.measuredHeight)
    val height = maxOf(textView.measuredHeight, editText.measuredHeight, imageSize)
    setMeasuredDimension(
      resolveSize(widthSize, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val imageHeight = maxOf(imageEdit.measuredHeight, imageSave.measuredHeight)
    val imageWidth = maxOf(imageEdit.measuredWidth, imageSave.measuredWidth)
    textView.layoutLeftTop(
      width / 2 - textView.measuredWidth / 2,
      height / 2 - textView.measuredHeight / 2
    )
    editText.layoutLeftTop(
      width / 2 - editText.measuredWidth / 2,
      height / 2 - editText.measuredHeight / 2
    )
    val left = width - imageWidth - MarginSmall
    val top = height / 2 - imageHeight / 2
    imageEdit.layoutLeftTop(left, top)
    imageSave.layoutLeftTop(left, top)
  }
  
  private fun changeModeIfPossible() {
    if (mode == SHOWING) {
      changeModeToEditing()
    } else {
      assertThat(mode == EDITING)
      changeModeToShowing()
    }
  }
  
  private enum class ShowingMode {
    SHOWING, EDITING
  }
}