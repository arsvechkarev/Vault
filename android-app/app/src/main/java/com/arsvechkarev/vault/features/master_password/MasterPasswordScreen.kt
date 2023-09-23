package com.arsvechkarev.vault.features.master_password

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import buisnesslogic.PasswordStrength.MEDIUM
import buisnesslogic.PasswordStrength.SECURE
import buisnesslogic.PasswordStrength.STRONG
import buisnesslogic.PasswordStrength.WEAK
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.arg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.core.views.FixedHeightTextView
import com.arsvechkarev.vault.core.views.PasswordStrengthMeter
import com.arsvechkarev.vault.core.views.TextWithQuestion
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.PasswordStrengthDialog.Companion.PasswordStrengthDialog
import com.arsvechkarev.vault.features.common.dialogs.PasswordStrengthDialog.Companion.passwordStrengthDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.master_password.MasterPasswordNews.FinishingSavingMasterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreenMode.CHANGE_EXISTING
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreenMode.CREATING_NEW
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnCancelChangePassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnConfirmChangePassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnHidePasswordStrengthDialog
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnInitialPasswordTyping
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnProceedWithWeakPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnRepeatPasswordTyping
import com.arsvechkarev.vault.features.master_password.MasterPasswordUiEvent.OnShowPasswordStrengthDialog
import com.arsvechkarev.vault.features.master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.master_password.PasswordEnteringState.REPEATING
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.EMPTY
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.OK
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.PASSWORDS_DONT_MATCH
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.PASSWORD_SAME_AS_CURRENT
import com.arsvechkarev.vault.features.master_password.UiPasswordStatus.TOO_WEAK
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.text
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class MasterPasswordScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout(MatchParent, MatchParent) {
      HorizontalLayout(MatchParent, WrapContent) {
        id(RepeatPasswordLayout)
        invisible()
        margins(top = MarginNormal + StatusBarHeight, start = MarginNormal, end = MarginNormal)
        ImageView(WrapContent, WrapContent, style = IconBack) {
          onClick { store.tryDispatch(OnBackPressed) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_repeat_password)
          margins(start = MarginLarge, end = MarginLarge)
          gravity(CENTER)
          textSize(TextSizes.H1)
        }
      }
      TextView(MatchParent, WrapContent, style = BoldTextView) {
        id(TextTitle)
        margins(top = MarginNormal + StatusBarHeight, start = MarginNormal, end = MarginNormal)
        gravity(CENTER)
        textSize(TextSizes.H1)
      }
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(CENTER)
        child<FixedHeightTextView>(WrapContent, WrapContent, style = BoldTextView) {
          id(TextPasswordStrength)
          margins(start = MarginNormal)
        }
        child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
          classNameTag()
          margins(top = MarginNormal, start = MarginNormal,
            end = MarginNormal, bottom = MarginLarge)
        }
        child<ViewSwitcher>(MatchParent, WrapContent) {
          classNameTag()
          inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
          outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
          child<EditTextPassword>(MatchParent, WrapContent) {
            id(EditTextEnterPassword)
            marginHorizontal(MarginNormal - MarginTiny)
            setHint(R.string.hint_enter_password)
            onTextChanged { text ->
              store.tryDispatch(OnInitialPasswordTyping(text))
            }
          }
          child<EditTextPassword>(MatchParent, WrapContent) {
            id(EditTextRepeatPassword)
            marginHorizontal(MarginNormal - MarginTiny)
            setHint(R.string.hint_repeat_password)
            onTextChanged { text ->
              store.tryDispatch(OnRepeatPasswordTyping(text))
            }
          }
        }
        child<TextWithQuestion>(MatchParent, WrapContent) {
          classNameTag()
          margins(start = MarginNormal, end = MarginNormal, top = MarginSmall)
          onClick { store.tryDispatch(OnShowPasswordStrengthDialog) }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(TextContinue)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_continue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        onClick { store.tryDispatch(OnContinueClicked) }
      }
      LoadingDialog()
      InfoDialog()
      PasswordStrengthDialog {
        onHide = { store.tryDispatch(OnHidePasswordStrengthDialog) }
        onChangePasswordClicked { store.tryDispatch(OnHidePasswordStrengthDialog) }
        onProceedClicked { store.tryDispatch(OnProceedWithWeakPassword) }
      }
    }
  }
  
  private var passwordEnteringState = INITIAL
  
  private val store by viewModelStore {
    MasterPasswordStore(coreComponent, arg(MasterPasswordScreenMode::class))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
  }
  
  override fun onAppearedOnScreen() {
    requireView().postDelayed({
      viewAs<EditTextPassword>(EditTextEnterPassword).showKeyboard()
    }, Durations.DelayOpenKeyboard)
  }
  
  private fun render(state: MasterPasswordState) {
    when (state.mode) {
      CREATING_NEW -> textView(TextTitle).text(R.string.text_create_master_password)
      CHANGE_EXISTING -> textView(TextTitle).text(R.string.text_change_master_password)
    }
    if (passwordEnteringState != state.passwordEnteringState) {
      passwordEnteringState = state.passwordEnteringState
      when (passwordEnteringState) {
        INITIAL -> switchToEnterPasswordState()
        REPEATING -> switchToRepeatPasswordState()
      }
    }
    if (state.showPasswordChangeConfirmationDialog) {
      infoDialog.showWithCancelAndProceedOption(
        titleRes = R.string.text_confirmation,
        messageRes = getString(R.string.text_confirmation_message),
        proceedTextRes = R.string.text_continue_caps,
        onCancel = { store.tryDispatch(OnCancelChangePassword) },
        onProceed = { store.tryDispatch(OnConfirmChangePassword) }
      )
    } else {
      infoDialog.hide()
    }
    if (state.showPasswordTooWeakDialog) {
      passwordStrengthDialog.show()
    } else {
      passwordStrengthDialog.hide()
    }
    showPasswordStatus(state.passwordStatus)
    showPasswordStrength(state)
  }
  
  private fun handleNews(event: MasterPasswordNews) {
    if (event is FinishingSavingMasterPassword) {
      requireContext().hideKeyboard()
      loadingDialog.show()
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private fun showPasswordStatus(passwordStatus: UiPasswordStatus) {
    val text = when (passwordStatus) {
      OK -> R.string.text_empty
      EMPTY -> R.string.text_password_cannot_be_empty
      TOO_WEAK -> R.string.text_password_is_too_weak
      PASSWORDS_DONT_MATCH -> R.string.text_passwords_dont_match
      PASSWORD_SAME_AS_CURRENT -> R.string.text_password_is_the_same_as_current
    }
    if (passwordStatus == TOO_WEAK) {
      viewAs<TextWithQuestion>().showQuestion()
    } else {
      viewAs<TextWithQuestion>().hideQuestion()
    }
    viewAs<TextWithQuestion>().setText(text)
  }
  
  private fun showPasswordStrength(state: MasterPasswordState) {
    if (state.passwordEnteringState == REPEATING) {
      textView(TextPasswordStrength).clearText()
      return
    }
    viewAs<PasswordStrengthMeter>().setStrength(state.passwordStrength)
    val textResId = when (state.passwordStrength) {
      WEAK -> R.string.text_weak
      MEDIUM -> R.string.text_medium
      STRONG -> R.string.text_strong
      SECURE -> R.string.text_secure
      null -> R.string.text_empty
    }
    textView(TextPasswordStrength).text(textResId)
  }
  
  private fun switchToEnterPasswordState() {
    viewAs<PasswordStrengthMeter>().setStrength(null, animate = false)
    viewAs<PasswordStrengthMeter>().visible()
    view(TextTitle).animateVisible()
    view(RepeatPasswordLayout).animateInvisible()
    textView(TextPasswordStrength).animateVisible()
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
      outAnimation = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_out_right)
      showPrevious()
    }
  }
  
  private fun switchToRepeatPasswordState() {
    viewAs<EditTextPassword>(EditTextRepeatPassword).clearText()
    textView(TextPasswordStrength).animateInvisible()
    view(TextTitle).animateInvisible()
    view(RepeatPasswordLayout).animateVisible()
    viewAs<PasswordStrengthMeter>().invisible()
    viewAs<ViewSwitcher>().apply {
      inAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_out_left)
      outAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
      showNext()
    }
  }
  
  companion object {
    
    val TextPasswordStrength = View.generateViewId()
    val TextTitle = View.generateViewId()
    val TextContinue = View.generateViewId()
    val RepeatPasswordLayout = View.generateViewId()
    val EditTextEnterPassword = View.generateViewId()
    val EditTextRepeatPassword = View.generateViewId()
  }
}
