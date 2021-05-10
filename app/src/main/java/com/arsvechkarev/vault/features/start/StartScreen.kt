package com.arsvechkarev.vault.features.start

import android.content.Context
import android.view.Gravity.CENTER
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.R.id.start_screen_enter_password
import com.arsvechkarev.vault.R.id.start_screen_error_text
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.hideKeyboard
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.showKeyboard
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen

class StartScreen : BaseScreen(), StartView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
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
        child<EditTextPassword>(MatchParent, WrapContent) {
          id(start_screen_enter_password)
          marginHorizontal(MarginDefault)
          setHint(R.string.hint_enter_password)
          onTextChanged { textView(start_screen_error_text).text("") }
          onSubmit { text -> presenter.onEnteredPassword(text) }
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
          val text = viewAs<EditTextPassword>(start_screen_enter_password).getText()
          presenter.onEnteredPassword(text)
        }
      }
      LoadingDialog()
    }
  }
  
  private val presenter by moxyPresenter {
    CoreComponent.instance.getStartComponentFactory().create().providePresenter()
  }
  
  override fun showKeyboard() {
    viewAs<EditTextPassword>(start_screen_enter_password).requestEditTextFocus()
    contextNonNull.showKeyboard()
  }
  
  override fun showLoadingCheckingPassword() {
    loadingDialog.show()
  }
  
  override fun showFailureCheckingPassword() {
    textView(start_screen_error_text).text(R.string.text_password_is_incorrect)
    loadingDialog.hide()
  }
  
  override fun showSuccessCheckingPassword() {
    contextNonNull.hideKeyboard()
  }
}