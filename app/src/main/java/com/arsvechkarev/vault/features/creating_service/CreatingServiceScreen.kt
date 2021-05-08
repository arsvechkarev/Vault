package com.arsvechkarev.vault.features.creating_service

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageView.ScaleType.FIT_XY
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.viewbuilding.Colors
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
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.clearImage
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.hideKeyboard
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
import com.arsvechkarev.vault.viewdsl.setSoftInputMode
import com.arsvechkarev.vault.viewdsl.showKeyboard
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.SimpleDialog
import com.arsvechkarev.vault.views.drawables.LetterInCircleDrawable.Companion.setLetterDrawable
import navigation.BaseScreen
import kotlin.math.abs

class CreatingServiceScreen : BaseScreen(), CreatingServiceView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      backgroundColor(Colors.Background)
      addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
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
          onClick { presenter.onBackClicked() }
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
          onTextChanged { presenter.onServiceNameChanged(it) }
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
    }
  }
  
  private val presenter by moxyPresenter {
    CoreComponent.instance.getCreatingServiceComponentFactory().create().providePresenter()
  }
  
  private val passwordTextWatcher = object : BaseTextWatcher {
    
    override fun onTextChange(text: String) {
      textView(TextError).text("")
    }
  }
  
  override fun onInit() {
    editText(EditTextServiceName).addTextChangedListener(passwordTextWatcher)
  }
  
  override fun onAppearedOnScreenAfterAnimation() {
    editText(EditTextServiceName).requestFocus()
    contextNonNull.showKeyboard()
  }
  
  override fun onRelease() {
    super.onRelease()
    editText(EditTextServiceName).removeTextChangedListener(passwordTextWatcher)
    contextNonNull.hideKeyboard()
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  override fun showServiceNameCannotBeEmpty() {
    textView(TextError).text(R.string.text_service_name_cannot_be_empty)
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
  
  override fun showLoadingCreation() {
    viewAs<SimpleDialog>(DialogProgressBar).show()
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