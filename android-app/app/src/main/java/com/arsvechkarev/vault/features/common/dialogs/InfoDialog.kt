package com.arsvechkarev.vault.features.common.dialogs

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.SimpleDialog
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Dimens.CornerRadiusDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableErrorTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.gone
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.size
import viewdsl.text
import viewdsl.textView
import viewdsl.visible
import viewdsl.withViewBuilder

class InfoDialog(context: Context) : SimpleDialog(context) {
  
  init {
    withViewBuilder {
      VerticalLayout(WrapContent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        minimumWidth = (context.resources.displayMetrics.widthPixels / 1.5).toInt()
        marginHorizontal(MarginExtraLarge)
        backgroundRoundRect(CornerRadiusDefault, Dialog)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginNormal, start = MarginNormal)
          id(DialogInfoTitle)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          id(DialogInfoMessage)
          margins(top = MarginLarge, start = MarginNormal, end = MarginNormal)
        }
        HorizontalLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.END)
          margins(top = MarginLarge, bottom = MarginNormal, start = MarginSmall,
            end = MarginSmall
          )
          TextView(WrapContent, WrapContent, style = ClickableTextView()) {
            id(DialogInfoText1)
          }
          TextView(WrapContent, WrapContent, style = ClickableTextView()) {
            id(DialogInfoText2)
          }
        }
      }
    }
  }
  
  fun showWithOkOption(
    titleRes: Int,
    messageRes: Int,
    textPositiveRes: Int,
    onCancel: () -> Unit = { hide() },
    onOkClicked: () -> Unit = { hide() }
  ) {
    show()
    this.onHide = onCancel
    textView(DialogInfoTitle).text(titleRes)
    textView(DialogInfoMessage).text(messageRes)
    textView(DialogInfoText1).gone()
    textView(DialogInfoText2).apply(ClickableTextView())
    textView(DialogInfoText2).text(textPositiveRes)
    textView(DialogInfoText2).onClick(onOkClicked)
  }
  
  fun showWithCancelAndProceedOption(
    titleRes: Int,
    messageRes: CharSequence,
    cancelTextRes: Int = R.string.text_cancel,
    proceedTextRes: Int = R.string.text_delete,
    showProceedAsError: Boolean = true,
    onCancel: () -> Unit = { hide() },
    onProceed: () -> Unit = {},
  ) {
    show()
    this.onHide = onCancel
    textView(DialogInfoTitle).text(titleRes)
    textView(DialogInfoMessage).text(messageRes)
    textView(DialogInfoText1).apply(ClickableTextView())
    textView(DialogInfoText1).visible()
    textView(DialogInfoText1).text(cancelTextRes)
    textView(DialogInfoText1).onClick(onCancel)
    val style = if (showProceedAsError) ClickableErrorTextView else ClickableTextView()
    textView(DialogInfoText2).apply(style)
    textView(DialogInfoText2).text(proceedTextRes)
    textView(DialogInfoText2).onClick(onProceed)
  }
  
  companion object {
    
    val DialogInfoTitle = View.generateViewId()
    val DialogInfoText1 = View.generateViewId()
    val DialogInfoText2 = View.generateViewId()
    val DialogInfoMessage = View.generateViewId()
    
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
