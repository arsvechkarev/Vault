package com.arsvechkarev.vault.features.creating_password

import android.content.Context
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.END
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import buisnesslogic.DEFAULT_PASSWORD_LENGTH
import buisnesslogic.MAX_PASSWORD_LENGTH
import buisnesslogic.MIN_PASSWORD_LENGTH
import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.core.views.CheckmarkAndTextViewGroup.Companion.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.core.views.PasswordStrengthMeterWithText
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.ShowGeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.EditPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode.NewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnBackClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnConfirmPasswordSavingClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnDeclinePasswordSavingClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnGeneratePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordLengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnSavePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledNumbers
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledSpecialSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledUppercaseSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.SetupCompleted
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageBackMargin
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.Styles.IconCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.classNameTag
import viewdsl.clearText
import viewdsl.drawablePadding
import viewdsl.drawables
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onProgressChanged
import viewdsl.onSubmit
import viewdsl.onTextChanged
import viewdsl.setMaxLength
import viewdsl.setSoftInputMode
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class CreatingPasswordScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      id(Root)
      VerticalLayout(MatchParent, MatchParent) {
        FrameLayout(MatchParent, WrapContent) {
          margins(top = MarginSmall + StatusBarHeight)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            id(Title)
            textSize(TextSizes.H1)
            layoutGravity(CENTER)
          }
          ImageView(WrapContent, WrapContent, style = IconCross) {
            margins(end = ImageBackMargin, top = MarginSmall, bottom = MarginSmall)
            layoutGravity(CENTER or END)
            onClick { store.tryDispatch(OnBackClicked) }
          }
        }
        TextView(WrapContent, WrapContent, style = Styles.BaseTextView) {
          id(TextError)
          layoutGravity(CENTER)
          gravity(CENTER)
          drawablePadding(MarginNormal)
          textColor(Colors.Error)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText()) {
          id(EditTextPassword)
          gravity(CENTER)
          margin(MarginSmall)
          setMaxLength(MAX_PASSWORD_LENGTH)
          isSingleLine = false
          onSubmit { store.tryDispatch(OnSavePasswordClicked) }
          onTextChanged { store.tryDispatch(OnPasswordChanged(it)) }
        }
        child<PasswordStrengthMeterWithText>(MatchParent, WrapContent) {
          classNameTag()
          margin(MarginNormal)
        }
        val commonBlock: CheckmarkAndTextViewGroup.() -> Unit = {
          onClick {
            val event = when (id) {
              R.string.text_uppercase_symbols -> OnToggledUppercaseSymbols
              R.string.text_numbers -> OnToggledNumbers
              R.string.text_special_symbols -> OnToggledSpecialSymbols
              else -> error("Unknown tag")
            }
            store.tryDispatch(event)
          }
        }
        CheckmarkAndTextViewGroup(R.string.text_uppercase_symbols, commonBlock)
        CheckmarkAndTextViewGroup(R.string.text_numbers, commonBlock)
        CheckmarkAndTextViewGroup(R.string.text_special_symbols, commonBlock)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(TextPasswordLength)
          margins(start = MarginNormal, top = MarginExtraLarge)
          textSize(TextSizes.H3)
          text(context.getString(R.string.text_password_length, DEFAULT_PASSWORD_LENGTH))
        }
        child<SeekBar>(MatchParent, WrapContent) {
          classNameTag()
          margin(MarginNormal)
          max = MAX_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
          progress = DEFAULT_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
          onProgressChanged { progress ->
            store.tryDispatch(OnPasswordLengthChanged(progress + MIN_PASSWORD_LENGTH))
          }
        }
        TextView(WrapContent, WrapContent, style = ClickableTextView()) {
          margins(top = MarginNormal)
          layoutGravity(CENTER_HORIZONTAL)
          gravity(CENTER)
          drawables(start = R.drawable.ic_generate, color = Colors.AccentLight)
          drawablePadding(MarginSmall)
          textColor(Colors.AccentLight)
          text(context.getString(R.string.text_generate_password))
          onClick { store.tryDispatch(OnGeneratePasswordClicked) }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        layoutGravity(CENTER or BOTTOM)
        margin(MarginNormal)
        text(R.string.text_save_password)
        onClick { store.tryDispatch(OnSavePasswordClicked) }
      }
      InfoDialog()
    }
  }
  
  private val store by viewModelStore { CreatingPasswordStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
  }
  
  override fun onAppearedOnScreen() {
    requireContext().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    requireContext().hideKeyboard()
  }
  
  private fun render(state: CreatingPasswordState) {
    if (!state.setupCompleted) {
      when (state.mode) {
        is EditPassword -> showEditingPasswordMode(state.mode.password)
        NewPassword -> showCreatingPasswordMode()
        null -> Unit // Do nothing
      }
    }
    showPasswordLength(state.passwordLength)
    showPasswordStrength(state.passwordStrength)
    val checkmark: (Int) -> CheckmarkAndTextViewGroup = { textResId -> viewAs(textResId) }
    checkmark(R.string.text_uppercase_symbols).isChecked = state.uppercaseSymbolsEnabled
    checkmark(R.string.text_numbers).isChecked = state.numbersEnabled
    checkmark(R.string.text_special_symbols).isChecked = state.specialSymbolsEnabled
    if (state.showPasswordCantBeEmpty) {
      textView(TextError).text(R.string.text_password_cannot_be_empty)
    } else {
      textView(TextError).clearText()
    }
    if (state.showConfirmationDialog) {
      showPasswordAcceptingDialog()
    } else {
      hidePasswordAcceptingDialog()
    }
  }
  
  private fun handleNews(event: CreatingPasswordNews) {
    when (event) {
      is ShowGeneratedPassword -> {
        requireContext().hideKeyboard()
        editText(EditTextPassword).clearFocus()
        editText(EditTextPassword).text(event.password)
        editText(EditTextPassword).setSelection(event.password.length)
      }
    }
  }
  
  override fun onDisappearedFromScreen() {
    requireContext().hideKeyboard()
  }
  
  override fun onDisappearedFromScreenAfterAnimation() {
    requireContext().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }
  
  private fun showCreatingPasswordMode() {
    textView(Title).text(R.string.text_password)
    viewAs<SeekBar>().progress = DEFAULT_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
    store.tryDispatch(SetupCompleted)
  }
  
  private fun showEditingPasswordMode(password: String) {
    textView(Title).text(R.string.text_edit_password)
    editText(EditTextPassword).text(password)
    editText(EditTextPassword).setSelection(password.length)
    viewAs<SeekBar>().progress = password.length - MIN_PASSWORD_LENGTH
    store.tryDispatch(SetupCompleted)
  }
  
  private fun showPasswordLength(length: Int) {
    val text = requireContext().getString(R.string.text_password_length, length)
    textView(TextPasswordLength).text(text)
    viewAs<SeekBar>().progress = length - MIN_PASSWORD_LENGTH
  }
  
  private fun showPasswordStrength(strength: PasswordStrength?) {
    if (strength == null) return
    val textResId = when (strength) {
      PasswordStrength.WEAK -> R.string.text_weak
      PasswordStrength.MEDIUM -> R.string.text_medium
      PasswordStrength.STRONG -> R.string.text_strong
      PasswordStrength.SECURE -> R.string.text_secure
    }
    viewAs<PasswordStrengthMeterWithText>().setText(textResId)
    viewAs<PasswordStrengthMeterWithText>().setStrength(strength)
  }
  
  private fun showPasswordAcceptingDialog() {
    requireContext().hideKeyboard()
    infoDialog.showWithOkOption(
      R.string.text_saving_password,
      R.string.text_do_you_want_to_save_password,
      R.string.text_yes,
      onCancel = { store.tryDispatch(OnDeclinePasswordSavingClicked) },
      onOkClicked = { store.tryDispatch(OnConfirmPasswordSavingClicked) }
    )
  }
  
  private fun hidePasswordAcceptingDialog() {
    infoDialog.onHide = {}
    infoDialog.hide()
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackClicked)
    return true
  }
  
  companion object {
    
    val Root = View.generateViewId()
    val Title = View.generateViewId()
    val TextPasswordLength = View.generateViewId()
    val TextError = View.generateViewId()
    val EditTextPassword = View.generateViewId()
  }
}
