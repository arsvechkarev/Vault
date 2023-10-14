package com.arsvechkarev.vault.features.creating_password

import android.content.Context
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.END
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.core.views.CheckmarkAndTextViewGroup.Companion.CheckmarkAndTextViewGroup
import com.arsvechkarev.vault.core.views.PasswordStrengthMeterWithText
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.SetupExistingPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.SetupNewPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordNews.ShowGeneratedPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnGeneratePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnPasswordLengthChanged
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnSavePasswordClicked
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledNumbers
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledSpecialSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.OnToggledUppercaseSymbols
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.Setup
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordUiEvent.SetupCompleted
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageBackMargin
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageCross
import com.arsvechkarev.vault.viewbuilding.TextSizes
import domain.DEFAULT_PASSWORD_LENGTH
import domain.MAX_PASSWORD_LENGTH
import domain.MIN_PASSWORD_LENGTH
import domain.Password
import domain.PasswordStrength
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.classNameTag
import viewdsl.compoundDrawablePadding
import viewdsl.compoundDrawables
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
      id(CreatingPasswordScreenRoot)
      VerticalLayout(MatchParent, MatchParent) {
        FrameLayout(MatchParent, WrapContent) {
          margins(top = StatusBarHeight + MarginMedium)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            id(Title)
            textSize(TextSizes.H1)
            layoutGravity(CENTER)
          }
          ImageView(WrapContent, WrapContent, style = ImageCross) {
            margins(end = ImageBackMargin, top = MarginSmall, bottom = MarginSmall)
            layoutGravity(CENTER or END)
            onClick { store.tryDispatch(OnBackPressed) }
          }
        }
        EditText(MatchParent, WrapContent,
          style = BaseEditText(R.string.text_generate_or_type_password)) {
          id(EditTextPassword)
          gravity(CENTER)
          margin(MarginSmall)
          setMaxLength(MAX_PASSWORD_LENGTH)
          isSingleLine = false
          onSubmit { store.tryDispatch(OnSavePasswordClicked) }
          onTextChanged { store.tryDispatch(OnPasswordChanged(Password.create(it))) }
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
          compoundDrawables(start = R.drawable.ic_generate, color = Colors.AccentLight)
          compoundDrawablePadding(MarginSmall)
          textColor(Colors.AccentLight)
          text(context.getString(R.string.text_generate_password))
          onClick { store.tryDispatch(OnGeneratePasswordClicked) }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        layoutGravity(CENTER or BOTTOM)
        margin(MarginNormal)
        text(R.string.text_save)
        onClick { store.tryDispatch(OnSavePasswordClicked) }
      }
    }
  }
  
  private val store by viewModelStore { CreatingPasswordStore(coreComponent) }
  
  override fun onInit() {
    coreComponent.creatingPasswordSetupObserver.passwordModeFlow
        .onEach { configuration -> store.tryDispatch(Setup(configuration)) }
        .launchIn(lifecycleScope)
    store.subscribe(this, ::render, ::handleNews)
  }
  
  override fun onAppearedOnScreen() {
    requireContext().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    requireContext().hideKeyboard()
  }
  
  private fun render(state: CreatingPasswordState) {
    showPasswordLength(state.passwordLength)
    showPasswordStrength(state.passwordStrength)
    val checkmark: (Int) -> CheckmarkAndTextViewGroup = { textResId -> viewAs(textResId) }
    checkmark(R.string.text_uppercase_symbols).isChecked = state.uppercaseSymbolsEnabled
    checkmark(R.string.text_numbers).isChecked = state.numbersEnabled
    checkmark(R.string.text_special_symbols).isChecked = state.specialSymbolsEnabled
  }
  
  private fun handleNews(event: CreatingPasswordNews) {
    when (event) {
      SetupNewPassword -> {
        textView(Title).text(R.string.text_password)
        viewAs<SeekBar>().progress = DEFAULT_PASSWORD_LENGTH - MIN_PASSWORD_LENGTH
        store.tryDispatch(SetupCompleted)
      }
      is SetupExistingPassword -> {
        textView(Title).text(R.string.text_edit_password)
        editText(EditTextPassword).text(event.password.stringData)
        editText(EditTextPassword).setSelection(event.password.stringData.length)
        viewAs<SeekBar>().progress = event.password.stringData.length - MIN_PASSWORD_LENGTH
        store.tryDispatch(SetupCompleted)
      }
      is ShowGeneratedPassword -> {
        requireContext().hideKeyboard()
        editText(EditTextPassword).clearFocus()
        editText(EditTextPassword).text(event.password.stringData)
        editText(EditTextPassword).setSelection(event.password.stringData.length)
      }
    }
  }
  
  override fun onDisappearedFromScreen() {
    requireContext().hideKeyboard()
  }
  
  @Suppress("DEPRECATION")
  override fun onRelease() {
    requireContext().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
  }
  
  private fun showPasswordLength(length: Int) {
    val text = requireContext().getString(R.string.text_password_length, length)
    textView(TextPasswordLength).text(text)
    viewAs<SeekBar>().progress = length - MIN_PASSWORD_LENGTH
  }
  
  private fun showPasswordStrength(strength: PasswordStrength?) {
    val textResId = when (strength) {
      PasswordStrength.WEAK -> R.string.text_weak
      PasswordStrength.MEDIUM -> R.string.text_medium
      PasswordStrength.STRONG -> R.string.text_strong
      PasswordStrength.SECURE -> R.string.text_secure
      null -> R.string.text_empty
    }
    viewAs<PasswordStrengthMeterWithText>().setText(textResId)
    viewAs<PasswordStrengthMeterWithText>().setStrength(strength)
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  companion object {
    
    val CreatingPasswordScreenRoot = View.generateViewId()
    val Title = View.generateViewId()
    val TextPasswordLength = View.generateViewId()
    val EditTextPassword = View.generateViewId()
  }
}
