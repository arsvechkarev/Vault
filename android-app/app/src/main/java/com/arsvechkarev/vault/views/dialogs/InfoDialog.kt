package com.arsvechkarev.vault.views.dialogs

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginVerySmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableErrorTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.views.SimpleDialog
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.gone
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textView
import viewdsl.visible
import viewdsl.withViewBuilder

class InfoDialog(context: Context) : SimpleDialog(context) {
  
  init {
    withViewBuilder {
      VerticalLayout(WrapContent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        marginHorizontal(MarginExtraLarge)
        backgroundRoundRect(CornerRadiusDefault, Dialog)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginNormal, start = MarginNormal)
          tag(DialogInfoTitle)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          tag(DialogInfoMessage)
          margins(top = MarginLarge, start = MarginNormal, end = MarginNormal)
        }
        HorizontalLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.END)
          margins(top = MarginLarge, bottom = MarginNormal, start = MarginSmall,
            end = MarginSmall
          )
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
  
    val BaseFragmentScreen.infoDialog get() = viewAs<InfoDialog>()
  
    fun ViewGroup.InfoDialog(block: InfoDialog.() -> Unit = {}) = withViewBuilder {
      val infoDialog = InfoDialog(context)
      infoDialog.size(MatchParent, MatchParent)
      infoDialog.classNameTag()
      addView(infoDialog)
      infoDialog.apply(block)
    }
  }
}
