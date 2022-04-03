package com.arsvechkarev.vault.features.start

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.extensions.showToast
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.start.StartScreenSingleEvent.*
import com.arsvechkarev.vault.features.start.StartScreenUserAction.*
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.FingerprintIconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.constraints
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.hideKeyboard
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.isVisible
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.showKeyboard
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen

class StartScreen : BaseScreen(), MviView<StartScreenState> {

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
                    margin(MarginDefault)
                }
                TextView(WrapContent, WrapContent, style = BoldTextView) {
                    textSize(TextSizes.H0)
                    text(R.string.app_name)
                }
            }
            VerticalLayout(MatchParent, WrapContent) {
                id(ContentLayoutId)
                margins(top = MarginBig * 2)
                constraints {
                    centeredWithin(parent)
                }
                TextView(MatchParent, WrapContent, style = BaseTextView) {
                    id(TextErrorId)
                    margins(start = MarginDefault + MarginVerySmall, bottom = MarginSmall)
                    textColor(Colors.Error)
                }
                child<EditTextPassword>(MatchParent, WrapContent) {
                    id(EditTextPasswordId)
                    marginHorizontal(MarginDefault)
                    setHint(R.string.hint_enter_password)
                    onTextChanged { presenter.applyAction(OnEditTextTyping) }
                    onSubmit { text -> presenter.applyAction(OnEnteredPassword(text)) }
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
                margin(MarginMedium)
                invisible()
                onClick { presenter.applyAction(OnFingerprintIconClicked) }
            }
            TextView(MatchParent, WrapContent, style = ClickableButton()) {
                id(ContinueButtonId)
                text(R.string.text_continue)
                margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
                constraints {
                    startToStartOf(parent)
                    endToEndOf(parent)
                    bottomToBottomOf(parent)
                }
                onClick {
                    val text = viewAs<EditTextPassword>(EditTextPasswordId).getText()
                    presenter.applyAction(OnEnteredPassword(text))
                }
            }
            LoadingDialog()
        }
    }

    private val presenter by moxyPresenter {
        CoreComponent.instance.getStartComponentFactory().create().providePresenter()
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

    override fun renderSingleEvent(event: Any) {
        when (event as StartScreenSingleEvent) {
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