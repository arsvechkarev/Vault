package com.arsvechkarev.vault.features.creating_service

import android.content.Context
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageView.ScaleType.FIT_XY
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.common.setServiceIcon
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnBackPressed
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnContinueClicked
import com.arsvechkarev.vault.features.creating_service.CreatingServiceUserActions.OnServiceNameTextChanged
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
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.clearText
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
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import navigation.BaseScreen
import kotlin.math.abs

class CreatingServiceScreen : BaseScreen(), MviView<CreatingServiceState> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      backgroundColor(Colors.Background)
      addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        showOrHideImageBasedOnLayout()
      }
      HorizontalLayout(MatchParent, WrapContent) {
        id(ToolbarId)
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
          onClick { presenter.applyAction(OnBackPressed) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_new_service)
          textSize(TextSizes.H1)
        }
      }
      ImageView(ImageServiceNameSize, ImageServiceNameSize) {
        id(ServiceImageId)
        scaleType = FIT_XY
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          topToBottomOf(ToolbarId)
          bottomToTopOf(EditTextLayoutsId)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(EditTextLayoutsId)
        constraints {
          centeredWithin(parent)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          id(TextError)
          textColor(Error)
          margins(start = MarginDefault)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          id(EditTextServiceName)
          margins(top = MarginSmall, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_service_name)
          onTextChanged { text -> presenter.applyAction(OnServiceNameTextChanged(text)) }
          onSubmit { editText(EditTextUsername).requestFocus() }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          id(EditTextUsername)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_username_optional)
          onSubmit { editText(EditTextEmail).requestFocus() }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText) {
          id(EditTextEmail)
          margins(top = MarginDefault, start = MarginDefault, end = MarginDefault)
          setHint(R.string.text_email_optional)
          onSubmit { continueWithCreating() }
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        id(ButtonContinue)
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
  
  override fun onAppearedOnScreenAfterAnimation() {
    editText(EditTextServiceName).requestFocus()
    contextNonNull.showKeyboard()
  }
  
  override fun render(state: CreatingServiceState) {
    imageView(ServiceImageId).setServiceIcon(state.serviceName)
    if (state.showServiceIconCannotBeEmpty) {
      textView(TextError).text(R.string.text_service_name_cannot_be_empty)
    } else {
      textView(TextError).clearText()
    }
  }
  
  override fun onRelease() {
    super.onRelease()
    contextNonNull.hideKeyboard()
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  private fun showOrHideImageBasedOnLayout() {
    val imageHeight = imageView(ServiceImageId).height
    val spaceForImage = abs(view(TextError).top - view(ToolbarId).bottom)
    if (spaceForImage < imageHeight) {
      imageView(ServiceImageId).invisible()
    } else {
      imageView(ServiceImageId).visible()
    }
  }
  
  private fun continueWithCreating() {
    val serviceName = editText(EditTextServiceName).text.toString()
    val username = editText(EditTextUsername).text.toString()
    val email = editText(EditTextEmail).text.toString()
    presenter.applyAction(OnContinueClicked(serviceName, username, email))
  }
  
  private companion object {
    
    val ToolbarId = View.generateViewId()
    val EditTextLayoutsId = View.generateViewId()
    val ServiceImageId = View.generateViewId()
    val TextError = View.generateViewId()
    val ButtonContinue = View.generateViewId()
    val EditTextServiceName = View.generateViewId()
    val EditTextUsername = View.generateViewId()
    val EditTextEmail = View.generateViewId()
  }
}