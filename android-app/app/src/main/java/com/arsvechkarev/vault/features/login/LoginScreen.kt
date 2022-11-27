package com.arsvechkarev.vault.features.login

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.VaultApplication.Companion.AppMainCoroutineScope
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
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
import com.arsvechkarev.vault.core.views.EditTextPassword
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.clearText
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
class LoginScreen : BaseFragmentScreen() {
  
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
        child<EditTextPassword>(MatchParent, WrapContent) {
          id(EditTextPasswordId)
          marginHorizontal(MarginNormal)
          text("qwetu1233") // TODO (7/22/2022): DELETE THIS!!!
          setHint(R.string.hint_enter_password)
          onTextChanged { store.tryDispatch(OnTypingText) }
          onSubmit { text -> store.tryDispatch(OnEnteredPassword(text)) }
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(TextErrorId)
          margins(start = MarginNormal + MarginTiny, top = MarginSmall)
          textColor(Colors.Error)
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
          store.tryDispatch(OnEnteredPassword(text))
        }
      }
      LoadingDialog()
    }
  }
  
  private val store by viewModelStore { LoginStore(appComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  override fun onAppearedOnScreenAfterAnimation() {
    // TODO (8/6/2022): Maybe create custom scope for screens
    AppMainCoroutineScope.launch { store.dispatch(OnAppearedOnScreen) }
  }
  
  private fun render(state: LoginState) {
    if (state.isLoading) loadingDialog.show() else loadingDialog.hide()
    if (state.showPasswordIsIncorrect) {
      textView(TextErrorId).text(R.string.text_password_is_incorrect)
    } else {
      textView(TextErrorId).clearText()
    }
    if (state.showKeyboard) {
      requireContext().showKeyboard()
      viewAs<EditTextPassword>(EditTextPasswordId).requestEditTextFocus()
    } else {
      requireContext().hideKeyboard()
    }
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    requireContext().hideKeyboard()
  }
  
  private companion object {
    
    val LogoLayoutId = View.generateViewId()
    val ContentLayoutId = View.generateViewId()
    val TextErrorId = View.generateViewId()
    val EditTextPasswordId = View.generateViewId()
    val ContinueButtonId = View.generateViewId()
  }
}
