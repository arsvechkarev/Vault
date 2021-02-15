package com.arsvechkarev.vault.views.dialogs

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Dimens.DefaultCornerRadius
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
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textView
import com.arsvechkarev.vault.viewdsl.viewAs
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.SimpleDialog

class InfoDialog(
  context: Context,
  tagPrefix: String
) : FrameLayout(context) {
  
  private val dialogInfo = "${tagPrefix}DialogInfo"
  private val dialogInfoTitle = "${tagPrefix}DialogInfoTitle"
  private val dialogInfoText1 = "${tagPrefix}DialogErrorText1"
  private val dialogInfoText2 = "${tagPrefix}DialogErrorText2"
  private val dialogInfoMessage = "${tagPrefix}DialogInfoMessage"
  
  init {
    withViewBuilder {
      child<SimpleDialog>(MatchParent, MatchParent) {
        tag(dialogInfo)
        onHide = { this@InfoDialog.onHide() }
        VerticalLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.CENTER)
          padding(MarginDefault)
          marginHorizontal(MarginBig)
          backgroundRoundRect(DefaultCornerRadius, Dialog)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(dialogInfoTitle)
          }
          TextView(WrapContent, WrapContent, style = BaseTextView) {
            tag(dialogInfoMessage)
            margins(top = MarginMedium)
          }
          HorizontalLayout(WrapContent, WrapContent) {
            layoutGravity(Gravity.END)
            margins(top = MarginMedium)
            TextView(WrapContent, WrapContent, style = ClickableTextView()) {
              tag(dialogInfoText1)
              margins(end = MarginSmall)
            }
            TextView(WrapContent, WrapContent, style = ClickableTextView()) {
              tag(dialogInfoText2)
            }
          }
        }
      }
    }
  }
  
  var onHide = {}
  
  fun showWithOkOption(
    titleRes: Int,
    messageRes: Int,
    textPositiveRes: Int,
    onOkClicked: () -> Unit = { hide() }
  ) {
    viewAs<SimpleDialog>(dialogInfo).show()
    textView(dialogInfoTitle).text(titleRes)
    textView(dialogInfoMessage).text(messageRes)
    textView(dialogInfoText1).gone()
    textView(dialogInfoText2).apply(ClickableTextView())
    textView(dialogInfoText2).text(textPositiveRes)
    textView(dialogInfoText2).onClick(onOkClicked)
  }
  
  fun showWithDeleteAndCancelOption(
    titleRes: Int,
    messageRes: CharSequence,
    onDeleteClicked: () -> Unit,
  ) {
    viewAs<SimpleDialog>(dialogInfo).show()
    textView(dialogInfoTitle).text(titleRes)
    textView(dialogInfoMessage).text(messageRes)
    textView(dialogInfoText1).apply(ClickableTextView())
    textView(dialogInfoText1).visible()
    textView(dialogInfoText1).text(R.string.text_cancel)
    textView(dialogInfoText1).onClick { hide() }
    textView(dialogInfoText2).apply(ClickableErrorTextView)
    textView(dialogInfoText2).text(R.string.text_delete)
    textView(dialogInfoText2).onClick(onDeleteClicked)
  }
  
  fun hide() {
    viewAs<SimpleDialog>(dialogInfo).hide()
  }
  
  companion object {
    
    val Screen.infoDialog get() = viewAs<InfoDialog>()
    
    fun ViewGroup.InfoDialog(tagPrefix: String = "", block: InfoDialog.() -> Unit = {}) = withViewBuilder {
      val infoDialog = InfoDialog(context, tagPrefix)
      infoDialog.size(MatchParent, MatchParent)
      infoDialog.classNameTag()
      addView(infoDialog)
      infoDialog.apply(block)
    }
  }
}