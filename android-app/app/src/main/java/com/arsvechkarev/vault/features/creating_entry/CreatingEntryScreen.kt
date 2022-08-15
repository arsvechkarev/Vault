package com.arsvechkarev.vault.features.creating_entry

import android.content.Context
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageView.ScaleType.FIT_XY
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.extensions.moxyStore
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.core.setServiceIcon
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnBackButtonClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryUiEvent.OnWebsiteNameTextChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageServiceNameSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.circleRippleBackground
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onLayoutChanged
import viewdsl.onSubmit
import viewdsl.onTextChanged
import viewdsl.padding
import viewdsl.setSoftInputMode
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder
import kotlin.math.abs

class CreatingEntryScreen : BaseScreen(), MviView<CreatingEntryState, CreatingEntryState> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      onLayoutChanged {
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
          margin(MarginNormal)
          gravity(CENTER_VERTICAL)
          padding(IconPadding)
          circleRippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnBackButtonClicked) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_new_password)
          textSize(TextSizes.H1)
        }
      }
      ImageView(ImageServiceNameSize, ImageServiceNameSize) {
        id(ImageId)
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
        margins(top = ImageServiceNameSize * 2)
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TextWebsiteName)
          margins(start = MarginNormal)
          text(R.string.text_website_name)
        }
        EditText(MatchParent, WrapContent) {
          apply(BaseEditText(hint = R.string.hint_website_name))
          id(EditTextWebsiteName)
          // TODO (8/14/2022): Figure out problem with EditText margins
          margins(start = MarginNormal - MarginTiny, end = MarginNormal)
          onTextChanged { text -> store.tryDispatch(OnWebsiteNameTextChanged(text)) }
          onSubmit { editText(EditTextLogin).requestFocus() }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TextLogin)
          text(R.string.text_login)
          margins(start = MarginNormal, top = MarginLarge)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText(hint = R.string.hint_login)) {
          id(EditTextLogin)
          margins(start = MarginNormal - MarginTiny, end = MarginNormal)
          onSubmit { continueWithCreating() }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ButtonContinue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
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
  
  private val store by moxyStore { CreatingEntryStore(appComponent) }
  
  override fun onAppearedOnScreenAfterAnimation() {
    editText(EditTextWebsiteName).requestFocus()
    contextNonNull.showKeyboard(editText(EditTextWebsiteName))
  }
  
  override fun render(state: CreatingEntryState) {
    imageView(ImageId).setServiceIcon(state.websiteName)
  }
  
  override fun onRelease() {
    super.onRelease()
    contextNonNull.hideKeyboard()
    contextNonNull.setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  private fun showOrHideImageBasedOnLayout() {
    val imageHeight = imageView(ImageId).height
    val spaceForImage = abs(view(TextWebsiteName).top - view(ToolbarId).bottom)
    if (spaceForImage < imageHeight) {
      imageView(ImageId).invisible()
    } else {
      imageView(ImageId).visible()
    }
  }
  
  private fun continueWithCreating() {
    val websiteName = editText(EditTextWebsiteName).text.toString()
    val login = editText(EditTextLogin).text.toString()
    store.tryDispatch(OnContinueClicked(websiteName, login))
  }
  
  private companion object {
  
    val ToolbarId = View.generateViewId()
    val EditTextLayoutsId = View.generateViewId()
    val ImageId = View.generateViewId()
    val TextWebsiteName = View.generateViewId()
    val TextLogin = View.generateViewId()
    val ButtonContinue = View.generateViewId()
    val EditTextWebsiteName = View.generateViewId()
    val EditTextLogin = View.generateViewId()
  }
}
