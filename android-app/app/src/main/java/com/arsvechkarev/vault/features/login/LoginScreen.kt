package com.arsvechkarev.vault.features.login

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.extensions.moxyStore
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnAppearedOnScreen
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnteredPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnTypingText
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

// TODO (7/21/2022): Add support for entering code instead of master password
class LoginScreen : BaseScreen(), MviView<LoginState, Nothing> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      VerticalLayout(WrapContent, WrapContent) {
        id(LogoLayoutId)
        constraints {
          topToTopOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToTopOf(ContentLayoutId)
        }
        gravity(CENTER)
        ImageView(ImageLogoSize, ImageLogoSize) {
          image(R.mipmap.ic_launcher)
          margin(MarginNormal)
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          textSize(TextSizes.H0)
          text(R.string.app_name)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(ContentLayoutId)
        margins(top = MarginExtraLarge * 2)
        constraints {
          centeredWithin(parent)
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(TextErrorId)
          margins(start = MarginNormal + MarginTiny, bottom = MarginSmall)
          textColor(Colors.Error)
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          id(EditTextPasswordId)
          marginHorizontal(MarginNormal)
          text("qwerty123??") // TODO (7/22/2022): DELETE THIS!!!
          setHint(R.string.hint_enter_password)
          onTextChanged { store.dispatch(OnTypingText) }
          onSubmit { text -> store.dispatch(OnEnteredPassword(text)) }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ContinueButtonId)
        text(R.string.text_continue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        onClick {
          val text = viewAs<EditTextPassword>(EditTextPasswordId).getText()
          store.dispatch(OnEnteredPassword(text))
        }
      }
      LoadingDialog()
    }
  }
  
  private val store by moxyStore { LoginStore(appComponent) }
  
  override fun onAppearedOnScreen() {
    store.dispatch(OnAppearedOnScreen)
  }
  
  override fun render(state: LoginState) {
    if (state.isLoading) loadingDialog.show() else loadingDialog.hide()
    if (state.showPasswordIsIncorrect) {
      textView(TextErrorId).text(R.string.text_password_is_incorrect)
    } else {
      textView(TextErrorId).text("")
    }
    if (state.showKeyboard) {
      contextNonNull.showKeyboard()
      viewAs<EditTextPassword>(EditTextPasswordId).requestEditTextFocus()
    } else {
      contextNonNull.hideKeyboard()
    }
  }
  
  private companion object {
    
    val LogoLayoutId = View.generateViewId()
    val ContentLayoutId = View.generateViewId()
    val TextErrorId = View.generateViewId()
    val EditTextPasswordId = View.generateViewId()
    val ContinueButtonId = View.generateViewId()
  }
}