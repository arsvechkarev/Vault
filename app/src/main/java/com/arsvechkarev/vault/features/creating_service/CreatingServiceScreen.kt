package com.arsvechkarev.vault.features.creating_service

import android.graphics.drawable.Drawable
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageView.ScaleType.FIT_XY
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.Singletons.passwordCreatingPresenter
import com.arsvechkarev.vault.core.Singletons.servicesRepository
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.extensions.setSoftInputMode
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.features.creating_password.PasswordEditingDialog.Companion.PasswordEditingDialog
import com.arsvechkarev.vault.features.creating_password.PasswordEditingDialog.Companion.passwordEditingDialog
import com.arsvechkarev.vault.viewbuilding.Colors.Error
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageServiceNameSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.BaseTextWatcher
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.clearImage
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.onSubmit
import com.arsvechkarev.vault.viewdsl.onTextChanged
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.drawables.LetterInCircleDrawable.Companion.setLetterDrawable
import kotlin.math.abs

class CreatingServiceScreen : Screen(), CreatingServiceView {
  
  override fun buildLayout() = withViewBuilder {
    RootConstraintLayout {
      addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        showOrHideImageBasedOnLayout()
      }
      HorizontalLayout(MatchParent, WrapContent) {
        id(R.id.creating_service_toolbar)
        margins(top = StatusBarHeight)
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
      ImageView(ImageServiceNameSize, ImageServiceNameSize) {
        id(R.id.creating_service_image)
        scaleType = FIT_XY
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          topToBottomOf(R.id.creating_service_toolbar)
          bottomToTopOf(R.id.creating_service_edit_texts_layout)
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
          margins(start = MarginDefault)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          tag(EditTextServiceName)
          margins(top = MarginSmall, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_service_name)
          whenPresenterIsReady { onTextChanged(presenter::onServiceNameChanged) }
          onSubmit { editText(EditTextUsername).requestFocus() }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          tag(EditTextUsername)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_username_optional)
          onSubmit { editText(EditTextEmail).requestFocus() }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          tag(EditTextEmail)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_email_optional)
          onSubmit { continueWithCreating() }
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
    CreatingServicePresenter(servicesRepository, AndroidThreader)
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      textView(TextError).text("")
    }
  }
  
  override fun onInit() {
    editText(EditTextServiceName).requestFocus()
    editText(EditTextServiceName).addTextChangedListener(passwordTextWatcher)
    showKeyboard()
  }
  
  override fun onRelease() {
    editText(EditTextServiceName).removeTextChangedListener(passwordTextWatcher)
    hideKeyboard()
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  override fun showServiceNameCannotBeEmpty() {
    textView(TextError).text(R.string.text_service_name_cannot_be_empty)
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
    infoDialog.showWithOkOption(
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
    infoDialog.hide()
  }
  
  override fun showIconFromResources(icon: Drawable) {
    showOrHideImageBasedOnLayout()
    imageView(R.id.creating_service_image).image(icon)
  }
  
  override fun showLetterInCircleIcon(letter: String) {
    showOrHideImageBasedOnLayout()
    imageView(R.id.creating_service_image).setLetterDrawable(letter)
  }
  
  override fun hideLetterInCircleIcon() {
    showOrHideImageBasedOnLayout()
    imageView(R.id.creating_service_image).clearImage()
  }
  
  override fun showExit() {
    navigator.popCurrentScreen()
  }
  
  override fun allowBackPress(): Boolean {
    return presenter.allowBackPress()
  }
  
  private fun showOrHideImageBasedOnLayout() {
    val imageHeight = imageView(R.id.creating_service_image).height
    val spaceForImage = abs(
      view(TextError).top - view(R.id.creating_service_toolbar).bottom)
    if (spaceForImage < imageHeight) {
      imageView(R.id.creating_service_image).invisible()
    } else {
      imageView(R.id.creating_service_image).visible()
    }
  }
  
  private fun continueWithCreating() {
    val serviceName = editText(EditTextServiceName).text.toString()
    val username = editText(EditTextUsername).text.toString()
    val email = editText(EditTextEmail).text.toString()
    presenter.onContinueClicked(serviceName, username, email)
  }
  
  companion object {
    
    const val TextError = "TextError"
    const val TextContinue = "TextContinue"
    const val EditTextServiceName = "EditTextServiceName"
    const val EditTextUsername = "EditTextUsername"
    const val EditTextEmail = "EditTextEmail"
    const val DialogProgressBar = "DialogProgressBar"
  }
}