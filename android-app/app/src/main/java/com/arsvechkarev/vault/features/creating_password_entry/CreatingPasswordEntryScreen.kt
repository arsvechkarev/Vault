package com.arsvechkarev.vault.features.creating_password_entry

import android.content.Context
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnContinueClicked
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryUiEvent.PasswordEntered
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onLayoutChanged
import viewdsl.onSubmit
import viewdsl.onTextChanged
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class CreatingPasswordEntryScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      onLayoutChanged {
        showOrHideViewsBasedOnLayout()
      }
      HorizontalLayout(MatchParent, WrapContent) {
        id(Toolbar)
        margins(top = StatusBarHeight)
        constraints {
          topToTopOf(parent)
        }
        ImageView(WrapContent, WrapContent, style = IconBack) {
          margin(MarginNormal)
          gravity(CENTER_VERTICAL)
          onClick { store.tryDispatch(OnBackPressed) }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          layoutGravity(CENTER)
          text(R.string.text_new_password)
          textSize(TextSizes.H1)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(EditTextLayouts)
        constraints {
          topToTopOf(parent)
        }
        margins(top = GradientDrawableHeight)
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleWebsiteName)
          margins(start = MarginNormal)
          text(R.string.text_website_name)
        }
        EditText(MatchParent, WrapContent) {
          apply(BaseEditText(hint = R.string.hint_website_name))
          id(EditTextWebsiteName)
          margins(start = MarginNormal, end = MarginNormal)
          onTextChanged { text -> store.tryDispatch(OnTitleTextChanged(text)) }
          onSubmit { editText(EditTextLogin).requestFocus() }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleLogin)
          text(R.string.text_login)
          margins(start = MarginNormal, top = MarginLarge)
        }
        EditText(MatchParent, WrapContent, style = BaseEditText(hint = R.string.hint_login)) {
          id(EditTextLogin)
          margins(start = MarginNormal, end = MarginNormal)
          onTextChanged { text -> store.tryDispatch(OnUsernameTextChanged(text)) }
          onSubmit { continueWithCreating() }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ButtonContinue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        constraints {
          bottomToBottomOf(parent)
        }
        text(R.string.text_continue)
        onClick { continueWithCreating() }
      }
    }
  }
  
  private val store by viewModelStore { CreatingPasswordEntryStore(coreComponent) }
  
  override fun onInit() {
    CreatingPasswordCommunication.communicator.output
        .onEach { event -> store.tryDispatch(PasswordEntered(event.password)) }
        .launchIn(lifecycleScope)
    store.subscribe(this, ::render)
  }
  
  override fun onAppearedOnScreen() {
    requireView().postDelayed({
      editText(EditTextWebsiteName).apply {
        requestFocus()
        requireContext().showKeyboard(this)
      }
    }, Durations.DelayOpenKeyboard)
  }
  
  private fun render(state: CreatingPasswordEntryState) {
    if (state.titleEmpty) {
      showAccentTextViewError(TitleWebsiteName, R.string.text_website_name_cant_be_empty)
    } else {
      showAccentTextViewDefault(TitleWebsiteName, R.string.text_website_name)
    }
    if (state.usernameEmpty) {
      showAccentTextViewError(TitleLogin, R.string.text_login_cant_be_empty)
    } else {
      showAccentTextViewDefault(TitleLogin, R.string.text_login)
    }
  }
  
  override fun onDisappearedFromScreen() {
    editText(EditTextWebsiteName).clearFocus()
    editText(EditTextLogin).clearFocus()
    requireContext().hideKeyboard()
  }
  
  private fun showOrHideViewsBasedOnLayout() {
    val continueButton = view(ButtonContinue)
    val marginBetweenButtonAndText = continueButton.top - view(EditTextLayouts).bottom
    if (marginBetweenButtonAndText < continueButton.height) {
      continueButton.invisible()
    } else {
      continueButton.visible()
    }
  }
  
  private fun continueWithCreating() {
    val websiteName = editText(EditTextWebsiteName).text.toString()
    val login = editText(EditTextLogin).text.toString()
    store.tryDispatch(OnContinueClicked(websiteName, login))
  }
  
  private fun showAccentTextViewDefault(textViewId: Int, @StringRes defaultTextRes: Int) {
    textView(textViewId).apply {
      apply(AccentTextView)
      text(defaultTextRes)
    }
  }
  
  private fun showAccentTextViewError(textViewId: Int, @StringRes errorTextRes: Int) {
    textView(textViewId).apply {
      textColor(Colors.Error)
      text(errorTextRes)
    }
  }
  
  companion object {
    
    val Toolbar = View.generateViewId()
    val EditTextLayouts = View.generateViewId()
    val TitleWebsiteName = View.generateViewId()
    val EditTextWebsiteName = View.generateViewId()
    val TitleLogin = View.generateViewId()
    val EditTextLogin = View.generateViewId()
    val ButtonContinue = View.generateViewId()
  }
}
