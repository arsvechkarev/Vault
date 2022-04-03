package com.arsvechkarev.vault.features.creating_master_password

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.animation.AnimationUtils
import android.widget.ViewSwitcher
import buisnesslogic.MIN_PASSWORD_LENGTH
import buisnesslogic.PasswordStatus
import buisnesslogic.PasswordStatus.*
import buisnesslogic.PasswordStrength
import buisnesslogic.PasswordStrength.*
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.CoreComponent
import com.arsvechkarev.vault.core.extensions.moxyPresenter
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreenUserActions.*
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordSingleEvents.FinishingAuthorization
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.INITIAL
import com.arsvechkarev.vault.features.creating_master_password.PasswordEnteringState.REPEATING
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.PasswordStrengthMeterHeight
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.*
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.Size.IntSize
import com.arsvechkarev.vault.views.EditTextPassword
import com.arsvechkarev.vault.views.PasswordStrengthMeter
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.PasswordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.PasswordStrengthDialog.Companion.passwordStrengthDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen

class CreatingMasterPasswordScreen : BaseScreen(), MviView<CreatingMasterPasswordScreenState> {

    override fun buildLayout(context: Context) = context.withViewBuilder {
        RootCoordinatorLayout(MatchParent, MatchParent) {
            backgroundColor(Colors.Background)
            HorizontalLayout(MatchParent, WrapContent) {
                tag(RepeatPasswordLayout)
                invisible()
                margins(
                    top = MarginDefault + StatusBarHeight,
                    start = MarginDefault,
                    end = MarginDefault
                )
                ImageView(WrapContent, WrapContent, style = ImageBack) {
                    onClick { presenter.applyAction(OnBackButtonClicked) }
                }
                TextView(WrapContent, WrapContent, style = BoldTextView) {
                    layoutGravity(CENTER)
                    text(R.string.text_repeat_password)
                    margins(start = MarginMedium, end = MarginMedium)
                    gravity(CENTER)
                    textSize(TextSizes.H1)
                }
            }
            TextView(MatchParent, WrapContent, style = BoldTextView) {
                tag(TextTitle)
                text(R.string.text_create_master_password)
                margins(
                    top = MarginDefault + StatusBarHeight,
                    start = MarginDefault,
                    end = MarginDefault
                )
                gravity(CENTER)
                textSize(TextSizes.H1)
            }
            VerticalLayout(MatchParent, WrapContent) {
                layoutGravity(CENTER)
                TextView(WrapContent, WrapContent, style = BaseTextView) {
                    tag(TextPasswordStrength)
                    margins(start = MarginDefault)
                }
                child<PasswordStrengthMeter>(MatchParent, IntSize(PasswordStrengthMeterHeight)) {
                    classNameTag()
                    margins(
                        top = MarginDefault, start = MarginDefault,
                        end = MarginDefault, bottom = MarginMedium
                    )
                }
                child<ViewSwitcher>(MatchParent, WrapContent) {
                    classNameTag()
                    inAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
                    outAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_right)
                    child<EditTextPassword>(MatchParent, WrapContent) {
                        tag(EditTextEnterPassword)
                        marginHorizontal(MarginDefault)
                        setHint(R.string.hint_enter_password)
                        onTextChanged { text -> presenter.applyAction(OnInitialPasswordTyping(text)) }
                    }
                    child<EditTextPassword>(MatchParent, WrapContent) {
                        tag(EditTextRepeatPassword)
                        marginHorizontal(MarginDefault)
                        setHint(R.string.hint_repeat_password)
                        onTextChanged { text -> presenter.applyAction(OnRepeatPasswordTyping(text)) }
                    }
                }
                TextView(WrapContent, WrapContent, style = BaseTextView) {
                    tag(TextError)
                    gravity(CENTER)
                    drawablePadding(MarginDefault)
                    drawables(end = R.drawable.ic_question, color = Colors.Background)
                    textColor(Colors.Error)
                    margins(start = MarginDefault, end = MarginDefault, top = MarginDefault)
                }
            }
            TextView(MatchParent, WrapContent, style = ClickableButton()) {
                tag(TextContinue)
                layoutGravity(Gravity.BOTTOM)
                text(R.string.text_continue)
                margins(start = MarginDefault, end = MarginDefault, bottom = MarginDefault)
                onClick { presenter.applyAction(OnContinueClicked) }
            }
            LoadingDialog()
            PasswordStrengthDialog {
                onHide = { presenter.applyAction(RequestHidePasswordStrengthDialog) }
                onGotItClicked { presenter.applyAction(RequestHidePasswordStrengthDialog) }
            }
        }
    }

    private var passwordEnteringState = INITIAL

    private val presenter by moxyPresenter {
        CoreComponent.instance.getCreatingMasterPasswordComponentFactory().create()
            .providePresenter()
    }

    override fun onAppearedOnScreenAfterAnimation() {
        contextNonNull.showKeyboard()
        viewAs<EditTextPassword>(EditTextEnterPassword).requestEditTextFocus()
    }

    override fun render(state: CreatingMasterPasswordScreenState) {
        if (passwordEnteringState != state.passwordEnteringState) {
            passwordEnteringState = state.passwordEnteringState
            when (passwordEnteringState) {
                INITIAL -> switchToEnterPasswordState()
                REPEATING -> switchToRepeatPasswordState()
            }
        }
        if (state.showPasswordStrengthDialog) {
            passwordStrengthDialog.show()
        } else {
            passwordStrengthDialog.hide()
        }
        state.passwordStatus?.let(::showPasswordStatus)
        showPasswordStrength(state.passwordStrength)
        if (state.passwordsMatch == false) {
            textView(TextError).text(R.string.text_passwords_dont_match)
        } else if (state.passwordsMatch == true) {
            textView(TextError).text("")
        }
    }

    override fun renderSingleEvent(event: Any) {
        if (event is FinishingAuthorization) {
            contextNonNull.hideKeyboard()
            loadingDialog.show()
        }
    }

    override fun handleBackPress(): Boolean {
        presenter.applyAction(OnBackPressed)
        return true
    }

    private fun showPasswordStatus(passwordStatus: PasswordStatus) {
        val text = when (passwordStatus) {
            EMPTY -> contextNonNull.getString(R.string.text_password_cannot_be_empty)
            TOO_SHORT -> contextNonNull.getString(
                R.string.text_password_min_length,
                MIN_PASSWORD_LENGTH
            )
            TOO_WEAK -> contextNonNull.getString(R.string.text_password_is_too_weak)
            OK -> contextNonNull.getString(R.string.text_empty)
        }
        textView(TextError).visible()
        if (passwordStatus == TOO_WEAK) {
            textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Error)
            textView(TextError).onClick { presenter.applyAction(RequestShowPasswordStrengthDialog) }
        } else {
            textView(TextError).drawables(end = R.drawable.ic_question, color = Colors.Background)
            textView(TextError).onClick {}
        }
        textView(TextError).text(text)
    }

    private fun showPasswordStrength(strength: PasswordStrength?) {
        viewAs<PasswordStrengthMeter>().setStrength(strength)
        val textResId = when (strength) {
            WEAK -> R.string.text_weak
            MEDIUM -> R.string.text_medium
            STRONG -> R.string.text_strong
            VERY_STRONG -> R.string.text_secure
            null -> R.string.text_empty
        }
        textView(TextPasswordStrength).text(textResId)
    }

    private fun switchToEnterPasswordState() {
        viewAs<PasswordStrengthMeter>().setStrength(null, animate = false)
        textView(TextError).text("")
        viewAs<PasswordStrengthMeter>().visible()
        view(TextTitle).animateVisible()
        view(RepeatPasswordLayout).animateInvisible()
        textView(TextPasswordStrength).animateVisible()
        textView(TextTitle).text(R.string.text_create_master_password)
        viewAs<ViewSwitcher>().apply {
            inAnimation = AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_in_left)
            outAnimation =
                AnimationUtils.loadAnimation(contextNonNull, android.R.anim.slide_out_right)
            showPrevious()
        }
    }

    private fun switchToRepeatPasswordState() {
        viewAs<EditTextPassword>(EditTextRepeatPassword).text("")
        textView(TextError).text("")
        textView(TextPasswordStrength).text("")
        textView(TextPasswordStrength).animateInvisible()
        view(TextTitle).animateInvisible()
        view(RepeatPasswordLayout).animateVisible()
        viewAs<PasswordStrengthMeter>().invisible()
        viewAs<ViewSwitcher>().apply {
            inAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_out_left)
            outAnimation = AnimationUtils.loadAnimation(contextNonNull, R.anim.slide_in_right)
            showNext()
        }
    }

    companion object {

        const val TextPasswordStrength = "TextPasswordStrength"
        const val TextError = "TextError"
        const val TextTitle = "TextTitle"
        const val TextContinue = "TextContinue"
        const val RepeatPasswordLayout = "RepeatPasswordLayout"
        const val EditTextEnterPassword = "EditTextEnterPassword"
        const val EditTextRepeatPassword = "EditTextRepeatPassword"
    }
}