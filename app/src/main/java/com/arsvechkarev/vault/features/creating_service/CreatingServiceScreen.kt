package com.arsvechkarev.vault.features.creating_service

import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Singletons.passwordCreatingPresenter
import com.arsvechkarev.vault.core.Singletons.passwordsListRepository
import com.arsvechkarev.vault.core.extensions.BaseTextWatcher
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.extensions.setSoftInputMode
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.features.creating_password.PasswordEditingDialog.Companion.PasswordEditingDialog
import com.arsvechkarev.vault.features.creating_password.PasswordEditingDialog.Companion.passwordEditingDialog
import com.arsvechkarev.vault.viewbuilding.Colors.Error
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.views.dialogs.LoadingDialog

class CreatingServiceScreen : Screen(), CreatingServiceView {
  
  override fun buildLayout() = withViewBuilder {
    RootConstraintLayout {
      HorizontalLayout(MatchParent, WrapContent) {
        id(R.id.creating_service_toolbar)
        constraints {
          topToTopOf(parent)
        }
        ImageView(WrapContent, WrapContent) {
          image(R.drawable.ic_back)
          margin(MarginDefault)
          gravity(CENTER_VERTICAL)
          padding(IconPadding)
          circleRippleBackground()
          onClick { navigator.popCurrentScreen() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_new_service)
          textSize(TextSizes.H1)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(R.id.creating_service_edit_texts_layout)
        constraints {
          centeredWithin(parent)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(TextError)
          textColor(Error)
          margins(start = MarginDefault + MarginSmall)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          tag(EditTextServiceName)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_service_name)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          tag(EditTextEmail)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_email_optional)
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        id(R.id.creating_service_continue_button)
        tag(TextContinue)
        margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        text(R.string.text_continue)
        onClick { continueWithCreating() }
      }
      PasswordEditingDialog(passwordCreatingPresenter) {
        onCloseClicked = { presenter.closePasswordEditingDialog() }
      }
      InfoDialog()
      LoadingDialog()
    }
  }
  
  private val presenter by moxyPresenter {
    CreatingServicePresenter(passwordsListRepository, AndroidThreader)
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      textView(TextError).text("")
    }
  }
  
  override fun onInit() {
    editText(EditTextServiceName).requestFocus()
    editText(EditTextEmail).addTextChangedListener(passwordTextWatcher)
    showKeyboard()
  }
  
  override fun onRelease() {
    editText(EditTextEmail).removeTextChangedListener(passwordTextWatcher)
    hideKeyboard()
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  override fun showServiceNameCannotBeEmpty() {
    textView(TextError).text(R.string.text_service_name_cannot_be_empty)
  }
  
  override fun showServiceNameAlreadyExists() {
    textView(TextError).text(getString(R.string.text_service_already_exists1))
  }
  
  override fun showPasswordCreatingDialog() {
    editText(EditTextServiceName).isEnabled = false
    editText(EditTextEmail).isEnabled = false
    passwordEditingDialog().initiatePasswordCreation(onSavePasswordClick = { password ->
      presenter.onPasswordEntered(password)
    })
    hideKeyboard()
  }
  
  override fun showLoadingCreation() {
    simpleDialog(DialogProgressBar).show()
  }
  
  override fun showDialogSavePassword() {
    infoDialog().show(
      R.string.text_saving_password,
      R.string.text_do_you_want_to_save_password,
      R.string.text_yes,
      onOkClicked = { presenter.acceptNewPassword() }
    )
  }
  
  override fun hidePasswordEditingDialog() {
    editText(EditTextServiceName).isEnabled = true
    editText(EditTextEmail).isEnabled = true
    passwordEditingDialog().hide()
  }
  
  override fun hideSavePasswordDialog() {
    infoDialog().hide()
  }
  
  override fun showExit() {
    navigator.popCurrentScreen()
  }
  
  override fun allowBackPress(): Boolean {
    return presenter.allowBackPress()
  }
  
  private fun continueWithCreating() {
    val serviceName = editText(EditTextServiceName).text.toString()
    val email = editText(EditTextEmail).text.toString()
    presenter.onContinueClicked(serviceName, email)
  }
  
  companion object {
    
    const val TextError = "TextError"
    const val TextContinue = "TextContinue"
    const val EditTextServiceName = "EditTextServiceName"
    const val EditTextEmail = "EditTextEmail"
    const val DialogProgressBar = "DialogProgressBar"
  }
}