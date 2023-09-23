package com.arsvechkarev.vault.features.change_master_password

import android.content.Context
import android.view.Gravity
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordDialogType.CONFIRMATION
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordDialogType.LOADING
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordDialogType.NOTIFICATION_AFTER
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordError.PASSWORDS_DO_NOT_MATCH
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordError.PASSWORD_SAME_AS_CURRENT
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnCancelChangePassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnChangeMasterPasswordClicked
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnConfirmChangePassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnInitialPasswordChanged
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnNotificationOkClicked
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordUiEvent.OnRepeatedPasswordChanged
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.clearText
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.parentView
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.viewAs
import viewdsl.withViewBuilder

class ChangeMasterPasswordScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    RootConstraintLayout {
      id(ChangeMasterPasswordScreenRoot)
      HorizontalLayout(MatchParent, WrapContent) {
        id(Toolbar)
        margins(top = StatusBarHeight + MarginNormal, start = MarginNormal, end = MarginNormal)
        constraints {
          topToTopOf(parent)
        }
        ImageView(WrapContent, WrapContent, style = IconBack) {
          margins(end = MarginNormal)
          gravity(Gravity.CENTER_VERTICAL)
          onClick { store.tryDispatch(OnBackPressed) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(Gravity.CENTER)
          text(R.string.text_change_master_password)
          textSize(TextSizes.H1)
        }
      }
      child<EditTextPassword>(MatchParent, WrapContent) {
        id(EditTextEnterNewPassword)
        setHint(R.string.hint_enter_password)
        onTextChanged { text -> store.tryDispatch(OnInitialPasswordChanged(text)) }
        onSubmit {
          parentView.viewAs<EditTextPassword>(EditTextRepeatPassword).requestEditTextFocus()
        }
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        constraints {
          bottomToTopOf(Guideline)
        }
      }
      View(MatchParent, IntSize(DividerHeight)) {
        id(Guideline)
        constraints {
          topToTopOf(parent)
          bottomToBottomOf(parent)
        }
      }
      child<EditTextPassword>(MatchParent, WrapContent) {
        id(EditTextRepeatPassword)
        setHint(R.string.hint_repeat_password)
        margins(start = MarginNormal, end = MarginNormal, top = MarginNormal)
        onTextChanged { text -> store.tryDispatch(OnRepeatedPasswordChanged(text)) }
        onSubmit { store.tryDispatch(OnChangeMasterPasswordClicked) }
        constraints {
          topToBottomOf(Guideline)
        }
      }
      TextView(MatchParent, WrapContent, style = BaseTextView) {
        id(TextError)
        textColor(Colors.Error)
        margins(start = MarginTiny + MarginNormal, top = MarginSmall)
        constraints {
          topToBottomOf(EditTextRepeatPassword)
          startToStartOf(EditTextRepeatPassword)
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ButtonChange)
        text(R.string.text_change)
        margin(MarginNormal)
        onClick { store.tryDispatch(OnChangeMasterPasswordClicked) }
        constraints {
          bottomToBottomOf(parent)
        }
      }
      InfoDialog { id(InfoDialog) }
      LoadingDialog { id(LoadingDialog) }
    }
  }
  
  private val store by viewModelStore { ChangeMasterPasswordStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  override fun onAppearedOnScreen() {
    viewAs<EditTextPassword>(EditTextEnterNewPassword).showKeyboard()
  }
  
  private fun render(state: ChangeMasterPasswordState) {
    if (state.error != null) {
      textView(TextError).setText(when (state.error) {
        PASSWORDS_DO_NOT_MATCH -> R.string.text_passwords_dont_match
        PASSWORD_SAME_AS_CURRENT -> R.string.text_password_is_the_same_as_current
      })
    } else {
      textView(TextError).clearText()
    }
    when (state.dialogType) {
      CONFIRMATION -> {
        infoDialog.showWithCancelAndProceedOption(
          titleRes = R.string.text_confirmation,
          messageRes = getString(R.string.text_confirmation_message),
          proceedTextRes = R.string.text_proceed,
          onCancel = { store.tryDispatch(OnCancelChangePassword) },
          onProceed = { store.tryDispatch(OnConfirmChangePassword) }
        )
      }
      
      LOADING -> {
        infoDialog.hide()
        loadingDialog.show()
      }
      
      NOTIFICATION_AFTER -> {
        loadingDialog.hide()
        infoDialog.showWithOkOption(
          titleRes = R.string.text_done,
          messageRes = R.string.text_master_password_changed,
          textPositiveRes = R.string.text_got_it,
          onCancel = { store.tryDispatch(OnNotificationOkClicked) },
          onOkClicked = { store.tryDispatch(OnNotificationOkClicked) }
        )
      }
      
      null -> {
        infoDialog.hide()
        loadingDialog.hide()
      }
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  companion object {
    
    val ChangeMasterPasswordScreenRoot = View.generateViewId()
    val Toolbar = View.generateViewId()
    val Guideline = View.generateViewId()
    val EditTextEnterNewPassword = View.generateViewId()
    val EditTextRepeatPassword = View.generateViewId()
    val TextError = View.generateViewId()
    val ButtonChange = View.generateViewId()
    val InfoDialog = View.generateViewId()
    val LoadingDialog = View.generateViewId()
  }
}
