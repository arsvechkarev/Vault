package com.arsvechkarev.vault.features.password_checking

import android.content.Context
import android.view.ViewGroup
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.MaterialProgressBar.Thickness.NORMAL
import com.arsvechkarev.vault.views.SimpleDialog
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.addView
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.hideKeyboard
import viewdsl.invisible
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.padding
import viewdsl.showKeyboard
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.textView
import viewdsl.viewAs
import viewdsl.withViewBuilder

class PasswordCheckingDialog(
  context: Context
) : SimpleDialog(context), PasswordCheckingView {
  
  init {
    onOutsideClick = { context.hideKeyboard() }
    withViewBuilder {
      VerticalLayout(MatchParent, WrapContent) {
        marginHorizontal(MarginExtraLarge)
        padding(MarginNormal)
        backgroundRoundRect(CornerRadiusDefault, Colors.Dialog)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          textSize(TextSizes.H3)
          text(R.string.text_enter_master_password)
        }
        val textError = TextView(WrapContent, WrapContent) {
          tag(TextErrorTag)
          textColor(Colors.Error)
          margins(top = MarginNormal)
        }
        val editTextPassword = child<EditTextPassword>(MatchParent, WrapContent) {
          classNameTag()
          setHint(R.string.hint_enter_password)
          onTextChanged { textError.text("") }
          //          onSubmit { text -> presenter.checkPassword(text) }
        }
        addView {
          MaterialProgressBar(context, thickness = NORMAL).apply {
            classNameTag()
            size(IconSize, IconSize)
            invisible()
          }
        }
        TextView(MatchParent, WrapContent, style = Button()) {
          tag(ContinueButtonTag)
          text(R.string.text_continue)
          margins(top = MarginNormal)
          //          onClick { presenter.checkPassword(editTextPassword.getText()) }
        }
      }
    }
  }
  
  override fun showDialog() {
    show()
    viewAs<EditTextPassword>().requestEditTextFocus()
    postDelayed({
      context.showKeyboard()
    }, 100)
  }
  
  override fun hideDialog() {
    hide()
    context.hideKeyboard()
    post {
      viewAs<EditTextPassword>().text("")
    }
  }
  
  override fun showPasswordCheckingLoading() {
    viewAs<EditTextPassword>().isEnabled = false
    textView(ContinueButtonTag).isEnabled = false
    viewAs<MaterialProgressBar>().animateVisible()
  }
  
  override fun showPasswordCheckingFinished() {
    viewAs<EditTextPassword>().isEnabled = true
    textView(ContinueButtonTag).isEnabled = true
    viewAs<MaterialProgressBar>().animateInvisible()
  }
  
  override fun showPasswordIsIncorrect() {
    textView(TextErrorTag).text(context.getString(R.string.text_password_is_incorrect))
  }
  
  companion object {
    
    private const val TextErrorTag = "TextError"
    private const val ContinueButtonTag = "ContinueButton"
    
    fun ViewGroup.PasswordCheckingDialog(block: PasswordCheckingDialog.() -> Unit = {}) = withViewBuilder {
      val infoDialog = PasswordCheckingDialog(context)
      infoDialog.size(MatchParent, MatchParent)
      infoDialog.classNameTag()
      addView(infoDialog)
      infoDialog.apply(block)
    }
  }
}
