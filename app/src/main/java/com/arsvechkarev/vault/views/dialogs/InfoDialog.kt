package com.arsvechkarev.vault.views.dialogs

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableErrorTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.gone
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textView
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.SimpleDialog

class InfoDialog(context: Context) : SimpleDialog(context) {
  
  init {
    withViewBuilder {
      VerticalLayout(WrapContent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        marginHorizontal(MarginBig)
        backgroundRoundRect(CornerRadiusDefault, Dialog)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginDefault, start = MarginDefault)
          tag(DialogInfoTitle)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(DialogInfoMessage)
          margins(top = MarginMedium, start = MarginDefault, end = MarginDefault)
        }
        HorizontalLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.END)
          margins(top = MarginMedium, bottom = MarginDefault, start = MarginSmall,
            end = MarginSmall)
          TextView(WrapContent, WrapContent, style = ClickableTextView()) {
            tag(DialogInfoText1)
            margins(end = HorizontalMarginVerySmall)
          }
          TextView(WrapContent, WrapContent, style = ClickableTextView()) {
            tag(DialogInfoText2)
          }
        }
      }
    }
  }
  
  fun showWithOkOption(
    titleRes: Int,
    messageRes: Int,
    textPositiveRes: Int,
    onOkClicked: () -> Unit = { hide() }
  ) {
    show()
    textView(DialogInfoTitle).text(titleRes)
    textView(DialogInfoMessage).text(messageRes)
    textView(DialogInfoText1).gone()
    textView(DialogInfoText2).apply(ClickableTextView())
    textView(DialogInfoText2).text(textPositiveRes)
    textView(DialogInfoText2).onClick(onOkClicked)
  }
  
  fun showWithDeleteAndCancelOption(
    titleRes: Int,
    messageRes: CharSequence,
    onDeleteClicked: () -> Unit,
  ) {
    show()
    textView(DialogInfoTitle).text(titleRes)
    textView(DialogInfoMessage).text(messageRes)
    textView(DialogInfoText1).apply(ClickableTextView())
    textView(DialogInfoText1).visible()
    textView(DialogInfoText1).text(R.string.text_cancel)
    textView(DialogInfoText1).onClick { hide() }
    textView(DialogInfoText2).apply(ClickableErrorTextView)
    textView(DialogInfoText2).text(R.string.text_delete)
    textView(DialogInfoText2).onClick(onDeleteClicked)
  }
  
  companion object {
    
    private const val DialogInfoTitle = "DialogInfoTitle"
    private const val DialogInfoText1 = "DialogErrorText1"
    private const val DialogInfoText2 = "DialogErrorText2"
    private const val DialogInfoMessage = "DialogInfoMessage"
    
    val Screen.infoDialog get() = viewAs<InfoDialog>()
    
    fun ViewGroup.InfoDialog(block: InfoDialog.() -> Unit = {}) = withViewBuilder {
      val infoDialog = InfoDialog(context)
      infoDialog.size(MatchParent, MatchParent)
      infoDialog.classNameTag()
      addView(infoDialog)
      infoDialog.apply(block)
    }
  }
}