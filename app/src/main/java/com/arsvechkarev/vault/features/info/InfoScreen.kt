package com.arsvechkarev.vault.features.info

import android.os.Bundle
import android.view.Gravity
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.AndroidThreader
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Ints.dp
import com.arsvechkarev.vault.viewdsl.Size
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.views.EditableTextInfoViewGroup
import com.arsvechkarev.vault.views.PasswordActionsView
import com.arsvechkarev.vault.views.drawables.LetterInCircleDrawable

class InfoScreen : Screen(), InfoView {
  
  override fun buildLayout() = withViewBuilder {
    val editableCommonBlock: EditableTextInfoViewGroup.() -> Unit = {
      size(MatchParent, WrapContent)
      marginHorizontal(MarginDefault)
      onEditClickAllowed = { !presenter.isEditingSomethingNow }
      onSwitchToEditMode = { presenter.switchToEditingMode() }
      onSaveClicked = { presenter.switchFromEditingMode() }
    }
    RootScrollableVerticalLayout {
      gravity(Gravity.CENTER_HORIZONTAL)
      ImageView(WrapContent, WrapContent, style = ImageBack) {
        layoutGravity(Gravity.NO_GRAVITY)
        onClick { navigator.popCurrentScreen() }
      }
      ImageView(70.dp, 70.dp) {
        tag(ImageServiceName)
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        text(R.string.text_service_name)
        margins(top = 32.dp)
        textColor(Colors.Accent)
      }
      addView {
        EditableTextInfoViewGroup(context).apply {
          tag(EditableTextInfoServiceName)
          apply(editableCommonBlock)
          onSaveClicked = { serviceName ->
            val letter = serviceName[0].toString()
            (imageView(ImageServiceName).drawable as LetterInCircleDrawable).setLetter(letter)
            presenter.switchFromEditingMode()
          }
        }
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        margins(top = 32.dp)
        textColor(Colors.Accent)
        text(R.string.text_email_optional)
      }
      addView {
        EditableTextInfoViewGroup(context).apply {
          tag(EditableTextInfoEmail)
          apply(editableCommonBlock)
        }
      }
      View(MatchParent, Size.IntSize(1.dp)) {
        backgroundColor(Colors.Divider)
        marginHorizontal(12.dp)
        margins(top = 32.dp)
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        text(R.string.text_password)
        margins(top = 16.dp)
        textColor(Colors.Accent)
        textSize(TextSizes.H3)
      }
      TextView(WrapContent, WrapContent, style = BoldTextView) {
        tag(TextPassword)
        margins(top = 8.dp)
        textSize(TextSizes.H2)
        text(context.getString(R.string.text_password_stub))
      }
      addView {
        PasswordActionsView(context).apply {
          classNameTag()
          size(MatchParent, WrapContent)
          margins(top = 24.dp)
        }
      }
    }
  }
  
  private val presenter by moxyPresenter {
    InfoPresenter(AndroidThreader)
  }
  
  override fun onInit(arguments: Bundle) {
    val serviceName = arguments.getString(KEY_SERVICE_NAME)!!
    val letter = serviceName[0].toString()
    val drawable = LetterInCircleDrawable(letter, Colors.TextPrimary, Colors.Accent)
    imageView(ImageServiceName).image(drawable)
    viewAs<EditableTextInfoViewGroup>(EditableTextInfoServiceName).setText(serviceName)
    val editableEmail = viewAs<EditableTextInfoViewGroup>(EditableTextInfoEmail)
    editableEmail.setText(contextNonNull.getString(R.string.text_no_email))
    editableEmail.transferTextToEditTextWhenSwitching = false
    viewAs<PasswordActionsView>().onTogglePassword = presenter::onTogglePassword
  }
  
  override fun showPassword(password: String) {
    textView(TextPassword).text(password)
  }
  
  override fun hidePassword() {
    textView(TextPassword).text(R.string.text_password_stub)
  }
  
  companion object {
    
    const val KEY_SERVICE_NAME = "KEY_SERVICE_NAME"
    
    const val ImageServiceName = "ImageServiceName"
    const val EditableTextInfoServiceName = "EditableTextInfoServiceName"
    const val EditableTextInfoEmail = "EditableTextInfoPassword"
    const val TextPassword = "TextPassword"
  }
}