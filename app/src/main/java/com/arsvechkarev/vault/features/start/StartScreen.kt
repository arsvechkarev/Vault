package com.arsvechkarev.vault.features.start

import android.view.Gravity.CENTER
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.R.id.start_screen_enter_password
import com.arsvechkarev.vault.R.id.start_screen_error_text
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Singletons.masterPasswordChecker
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.onSubmit
import com.arsvechkarev.vault.viewdsl.onTextChanged
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog

class StartScreen : Screen(), StartView {
  
  override fun buildLayout() = withViewBuilder {
    RootConstraintLayout {
      VerticalLayout(WrapContent, WrapContent) {
        id(R.id.start_screen_logo_layout)
        constraints {
          topToTopOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToTopOf(R.id.start_screen_content_layout)
        }
        gravity(CENTER)
        ImageView(ImageLogoSize, ImageLogoSize) {
          image(R.mipmap.ic_launcher)
          margin(MarginDefault)
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          textSize(TextSizes.H0)
          text(R.string.app_name)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(R.id.start_screen_content_layout)
        margins(top = MarginBig * 2)
        constraints {
          centeredWithin(parent)
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(start_screen_error_text)
          margins(start = MarginDefault + MarginVerySmall, bottom = MarginSmall)
          textColor(Colors.Error)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          id(start_screen_enter_password)
          marginHorizontal(MarginDefault)
          setHint(R.string.hint_enter_password)
          onTextChanged { textView(start_screen_error_text).text("") }
          onSubmit { presenter.onEnteredPassword(this.text.toString()) }
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        id(R.id.start_screen_continue)
        text(R.string.text_continue)
        margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        onClick {
          val text = editText(start_screen_enter_password).text.toString()
          presenter.onEnteredPassword(text)
        }
      }
      LoadingDialog()
    }
  }
  
  private val presenter by moxyPresenter {
    StartPresenter(masterPasswordChecker, AndroidThreader)
  }
  
  override fun onInit() {
    editText(start_screen_enter_password).requestFocus()
    showKeyboard()
  }
  
  override fun showLoading() {
    loadingDialog.show()
  }
  
  override fun showError() {
    textView(start_screen_error_text).text(R.string.text_password_is_incorrect)
    loadingDialog.hide()
  }
  
  override fun showSuccess() {
    hideKeyboard()
    navigator.goToPasswordsListScreen()
  }
}