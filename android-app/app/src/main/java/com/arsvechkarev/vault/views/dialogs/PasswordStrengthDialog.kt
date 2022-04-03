package com.arsvechkarev.vault.views.dialogs

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.*
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.views.SimpleDialog
import navigation.BaseScreen

class PasswordStrengthDialog(context: Context) : SimpleDialog(context) {

    init {
        withViewBuilder {
            VerticalLayout(WrapContent, WrapContent) {
                layoutGravity(CENTER)
                marginHorizontal(MarginBig)
                backgroundRoundRect(CornerRadiusDefault, Colors.Dialog)
                TextView(WrapContent, WrapContent, style = BoldTextView) {
                    margins(top = MarginDefault, start = MarginDefault)
                    text(R.string.text_password_strength)
                    textSize(TextSizes.H3)
                }
                TextView(WrapContent, WrapContent, style = BaseTextView) {
                    margins(top = MarginMedium, start = MarginDefault, end = MarginDefault)
                    textSize(TextSizes.H4)
                    text(R.string.text_password_should_be_strong)
                }
                TextView(WrapContent, WrapContent, style = ClickableTextView()) {
                    classNameTag()
                    layoutGravity(Gravity.END)
                    margins(
                        top = MarginMedium, bottom = MarginDefault,
                        start = MarginSmall, end = MarginSmall
                    )
                    text(R.string.text_got_it)
                }
            }
        }
    }

    fun onGotItClicked(block: () -> Unit) {
        viewAs<TextView>().onClick(block)
    }

    companion object {

        val BaseScreen.passwordStrengthDialog get() = viewAs<PasswordStrengthDialog>()

        fun ViewGroup.PasswordStrengthDialog(block: PasswordStrengthDialog.() -> Unit = {}) =
            withViewBuilder {
                child<PasswordStrengthDialog, ViewGroup.LayoutParams>(
                    MatchParent,
                    MatchParent,
                    block
                ) {
                    classNameTag()
                }
            }
    }
}