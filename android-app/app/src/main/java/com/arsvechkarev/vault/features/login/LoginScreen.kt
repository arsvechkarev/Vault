package com.arsvechkarev.vault.features.login

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import androidx.biometric.BiometricPrompt
import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.core.views.FixedHeightTextView
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.biometrics.BiometricsCryptography
import com.arsvechkarev.vault.features.common.biometrics.BiometricsPromptUtils
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.login.LoginNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnterWithBiometrics
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnterWithPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnInit
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
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder
import javax.crypto.Cipher

class LoginScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      VerticalLayout(WrapContent, WrapContent) {
        id(LogoLayout)
        constraints {
          topToTopOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToTopOf(ContentLayout)
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
        id(ContentLayout)
        margins(top = MarginExtraLarge * 2)
        constraints {
          centeredWithin(parent)
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          id(EditTextPassword)
          marginHorizontal(MarginNormal)
          if (BuildConfig.DEBUG) {
            setRawText(BuildConfig.STUB_PASSWORD)
            editText.setSelection(BuildConfig.STUB_PASSWORD.length)
          }
          setHint(R.string.text_enter_password)
          onTextChanged { store.tryDispatch(OnTypingText) }
          onSubmit { text -> store.tryDispatch(OnEnterWithPassword(text)) }
        }
        child<FixedHeightTextView>(MatchParent, WrapContent, style = BaseTextView) {
          id(TextError)
          margins(start = MarginNormal + MarginTiny, top = MarginSmall)
          textColor(Colors.Error)
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ButtonContinue)
        text(R.string.text_continue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        onClick {
          val password = viewAs<EditTextPassword>(EditTextPassword).getPassword()
          store.tryDispatch(OnEnterWithPassword(password))
        }
      }
      LoadingDialog()
    }
  }
  
  private val store by viewModelStore { LoginStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  override fun onAppearedOnScreen() {
    requireView().postDelayed({
      viewAs<EditTextPassword>(EditTextPassword).showKeyboard()
    }, Durations.DelayOpenKeyboard)
  }
  
  private fun render(state: LoginState) {
    if (state.showLoading) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
    if (state.showPasswordIsIncorrect) {
      textView(TextError).text(R.string.text_password_is_incorrect)
    } else {
      textView(TextError).clearText()
    }
  }
  
  private fun handleNews(news: LoginNews) {
    if (news is ShowBiometricsPrompt) {
      showBiometricPrompt(news)
    }
  }
  
  private fun showBiometricPrompt(news: ShowBiometricsPrompt) {
    val cipher = coreComponent.biometricsCipherProvider
        .getCipherForDecryption(news.iv)
    val biometricsPrompt = BiometricsPromptUtils.createBiometricPrompt(
      this,
      ::handleBiometricsSuccess,
      ::handleBiometricsFailure
    )
    val promptInfo = BiometricsPromptUtils.createPromptInfo(
      getText(R.string.text_enter_with_fingerprint),
      getText(R.string.text_biometrics_cancel),
    )
    biometricsPrompt.authenticate(promptInfo, BiometricPrompt.CryptoObject(cipher))
  }
  
  private fun handleBiometricsSuccess(cipher: Cipher) {
    val cryptography = BiometricsCryptography.create(cipher)
    store.tryDispatch(OnEnterWithBiometrics(cryptography))
  }
  
  private fun handleBiometricsFailure() {
    
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    requireContext().hideKeyboard()
  }
  
  companion object {
    
    val LogoLayout = View.generateViewId()
    val ContentLayout = View.generateViewId()
    val TextError = View.generateViewId()
    val EditTextPassword = View.generateViewId()
    val ButtonContinue = View.generateViewId()
  }
}
