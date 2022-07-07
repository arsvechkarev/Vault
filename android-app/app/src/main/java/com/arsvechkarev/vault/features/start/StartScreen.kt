package com.arsvechkarev.vault.features.start

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.showToast
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.start.StartScreenNews.ShowEditTextStubPassword
import com.arsvechkarev.vault.features.start.StartScreenNews.ShowPermanentLockout
import com.arsvechkarev.vault.features.start.StartScreenNews.ShowTooManyAttemptsTryAgainLater
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.FingerprintIconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class StartScreen : BaseScreen(), MviView<StartScreenState, StartScreenNews> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      VerticalLayout(WrapContent, WrapContent) {
        id(LogoLayoutId)
        constraints {
          topToTopOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToTopOf(ContentLayoutId)
        }
        gravity(CENTER)
        ImageView(ImageLogoSize, ImageLogoSize) {
          image(R.mipmap.ic_launcher)
          margin(MarginNormal)
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          textSize(TextSizes.H0)
          text(R.string.app_name)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(ContentLayoutId)
        margins(top = MarginExtraLarge * 2)
        constraints {
          centeredWithin(parent)
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          id(TextErrorId)
          margins(start = MarginNormal + MarginTiny, bottom = MarginSmall)
          textColor(Colors.Error)
        }
        child<EditTextPassword>(MatchParent, WrapContent) {
          id(EditTextPasswordId)
          marginHorizontal(MarginNormal)
          setHint(R.string.hint_enter_password)
          //          onTextChanged { presenter.applyAction(OnEditTextTyping) }
          //          onSubmit { text -> presenter.applyAction(OnEnteredPassword(text)) }
        }
      }
      ImageView(FingerprintIconSize, FingerprintIconSize) {
        id(FingerprintButtonId)
        constraints {
          bottomToTopOf(ContinueButtonId)
          startToStartOf(parent)
          endToEndOf(parent)
        }
        image(R.drawable.ic_fingerprint)
        margin(MarginLarge)
        invisible()
        //        onClick { presenter.applyAction(OnFingerprintIconClicked) }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ContinueButtonId)
        text(R.string.text_continue)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        constraints {
          startToStartOf(parent)
          endToEndOf(parent)
          bottomToBottomOf(parent)
        }
        onClick {
          val text = viewAs<EditTextPassword>(EditTextPasswordId).getText()
          //          presenter.applyAction(OnEnteredPassword(text))
        }
      }
      LoadingDialog()
    }
  }
  
  override fun render(state: StartScreenState) {
    if (state.isLoading) loadingDialog.show() else loadingDialog.hide()
    view(FingerprintButtonId).isVisible = state.showFingerprintIcon
    if (state.showPasswordIsIncorrect) {
      textView(TextErrorId).text(R.string.text_password_is_incorrect)
    } else {
      textView(TextErrorId).text("")
    }
    if (state.showKeyboard) {
      contextNonNull.showKeyboard()
      viewAs<EditTextPassword>(EditTextPasswordId).requestEditTextFocus()
    } else {
      contextNonNull.hideKeyboard()
    }
  }
  
  override fun handleNews(event: StartScreenNews) {
    when (event) {
      ShowPermanentLockout -> {
        showToast(R.string.text_biometrics_use_password)
      }
      ShowTooManyAttemptsTryAgainLater -> {
        showToast(R.string.text_biometrics_try_again_later)
      }
      ShowEditTextStubPassword -> {
        viewAs<EditTextPassword>(EditTextPasswordId).text(R.string.text_password_stub)
      }
    }
  }
  
  private companion object {
    
    val LogoLayoutId = View.generateViewId()
    val ContentLayoutId = View.generateViewId()
    val TextErrorId = View.generateViewId()
    val EditTextPasswordId = View.generateViewId()
    val FingerprintButtonId = View.generateViewId()
    val ContinueButtonId = View.generateViewId()
  }
}