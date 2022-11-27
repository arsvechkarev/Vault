package com.arsvechkarev.vault.features.common.dialogs

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.ViewGroup
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.core.views.SimpleDialog
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.text
import viewdsl.textSize
import viewdsl.viewAs
import viewdsl.withViewBuilder

class PasswordStrengthDialog(context: Context) : SimpleDialog(context) {
  
  init {
    withViewBuilder {
      VerticalLayout(WrapContent, WrapContent) {
        layoutGravity(CENTER)
        marginHorizontal(MarginExtraLarge)
        backgroundRoundRect(CornerRadiusDefault, Colors.Dialog)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginNormal, start = MarginNormal)
          text(R.string.text_password_strength)
          textSize(TextSizes.H3)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margins(top = MarginLarge, start = MarginNormal, end = MarginNormal)
          textSize(TextSizes.H4)
          text(R.string.text_password_should_be_strong)
        }
        TextView(WrapContent, WrapContent, style = ClickableTextView()) {
          classNameTag()
          layoutGravity(Gravity.END)
          margins(top = MarginLarge, bottom = MarginNormal,
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
  
    val BaseFragmentScreen.passwordStrengthDialog get() = viewAs<PasswordStrengthDialog>()
  
    fun ViewGroup.PasswordStrengthDialog(
      block: PasswordStrengthDialog.() -> Unit = {}
    ) = withViewBuilder {
      child<PasswordStrengthDialog, ViewGroup.LayoutParams>(MatchParent, MatchParent, block) {
        classNameTag()
      }
    }
  }
}
