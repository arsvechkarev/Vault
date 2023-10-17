package com.arsvechkarev.vault.features.login

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.core.views.FixedHeightTextView
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.biometrics.BiometricsDialog
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.login.LoginBiometricsState.LOCKOUT
import com.arsvechkarev.vault.features.login.LoginBiometricsState.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.login.LoginBiometricsState.NOT_ALLOWED
import com.arsvechkarev.vault.features.login.LoginBiometricsState.OK
import com.arsvechkarev.vault.features.login.LoginBiometricsState.OTHER_ERROR
import com.arsvechkarev.vault.features.login.LoginNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.login.LoginNews.ShowKeyboard
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnBiometricsEvent
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnBiometricsIconClicked
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnterWithPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnInit
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnTypingText
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.CircleButtonSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.clearText
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.isVisible
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class LoginScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      VerticalLayout(WrapContent, WrapContent) {
        id(LogoLayout)
        constraints {
          topToTopOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToTopOf(LayoutContent)
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
        id(LayoutContent)
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
      VerticalLayout(WrapContent, WrapContent) {
        id(LayoutBiometrics)
        gravity(CENTER)
        margins(start = MarginLarge, end = MarginLarge, bottom = MarginLarge)
        onClick { store.tryDispatch(OnBiometricsIconClicked) }
        constraints {
          setVerticalBias(0.8f)
          startToStartOf(parent)
          topToBottomOf(LayoutContent)
          endToEndOf(parent)
          bottomToTopOf(ButtonContinue)
        }
        ImageView(IntSize(CircleButtonSize), IntSize(CircleButtonSize)) {
          id(ImageBiometrics)
          image(R.drawable.ic_fingerprint)
          margins(bottom = MarginSmall)
        }
        TextView(WrapContent, WrapContent, style = SecondaryTextView) {
          id(TextBiometrics)
          gravity(CENTER)
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
  
  private val biometricsDialog by lazy {
    BiometricsDialog.create(this, R.string.text_enter_with_biometrics)
  }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
    biometricsDialog.events
        .onEach { event -> store.tryDispatch(OnBiometricsEvent(event)) }
        .launchIn(lifecycleScope)
  }
  
  private fun render(state: LoginState) {
    view(LayoutBiometrics).isVisible = state.biometricsEnabled
    if (state.showPasswordIsIncorrect) {
      textView(TextError).text(R.string.text_password_is_incorrect)
    } else {
      textView(TextError).clearText()
    }
    val textRes = when (state.biometricsState) {
      OK -> R.string.text_biometrics_tap_here
      NOT_ALLOWED -> R.string.text_biometrics_error_not_allowed
      LOCKOUT -> R.string.text_biometrics_error_lockout
      LOCKOUT_PERMANENT -> R.string.text_biometrics_error_lockout_permanent
      OTHER_ERROR -> R.string.text_biometrics_error_other
    }
    textView(TextBiometrics).text(textRes)
    if (state.showLoading) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
  }
  
  private fun handleNews(news: LoginNews) {
    when (news) {
      ShowKeyboard -> {
        requireView().postDelayed({
          viewAsNullable<EditTextPassword>(EditTextPassword)?.showKeyboard()
        }, Durations.DelayOpenKeyboard)
      }
      is ShowBiometricsPrompt -> {
        val cipher = coreComponent.biometricsCipherProvider.getCipherForDecryption(news.iv)
        biometricsDialog.launch(cipher)
      }
    }
  }
  
  override fun onDestroyView() {
    super.onDestroyView()
    requireContext().hideKeyboard()
  }
  
  companion object {
    
    val LogoLayout = View.generateViewId()
    val LayoutContent = View.generateViewId()
    val TextError = View.generateViewId()
    val EditTextPassword = View.generateViewId()
    val LayoutBiometrics = View.generateViewId()
    val ImageBiometrics = View.generateViewId()
    val TextBiometrics = View.generateViewId()
    val ButtonContinue = View.generateViewId()
  }
}
