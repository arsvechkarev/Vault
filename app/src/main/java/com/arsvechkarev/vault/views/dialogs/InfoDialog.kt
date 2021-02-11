package com.arsvechkarev.vault.views.dialogs

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Colors.Dialog
import com.arsvechkarev.vault.viewbuilding.Dimens.DefaultCornerRadius
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.backgroundRoundRect
import com.arsvechkarev.vault.viewdsl.childViewAs
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textColor
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.SimpleDialog

class InfoDialog(
  context: Context,
  tagPrefix: String
) : FrameLayout(context) {
  
  private val dialogInfo = "${tagPrefix}DialogInfo"
  private val dialogInfoTitle = "${tagPrefix}DialogInfoTitle"
  private val dialogInfoTextOk = "${tagPrefix}DialogErrorTextOk"
  private val dialogInfoMessage = "${tagPrefix}DialogInfoMessage"
  
  init {
    withViewBuilder {
      child<SimpleDialog>(MatchParent, MatchParent) {
        tag(dialogInfo)
        VerticalLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.CENTER)
          padding(MarginDefault)
          marginHorizontal(MarginBig)
          backgroundRoundRect(DefaultCornerRadius, Dialog)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(dialogInfoTitle)
            textSize(TextSizes.H3)
          }
          TextView(WrapContent, WrapContent, style = BaseTextView) {
            tag(dialogInfoMessage)
            margins(top = MarginMedium)
          }
          TextView(WrapContent, WrapContent, style = ClickableTextView()) {
            tag(dialogInfoTextOk)
            margins(top = MarginMedium)
            textColor(Colors.AccentLight)
            layoutGravity(Gravity.END)
            onClick {
            }
          }
        }
      }
    }
  }
  
  fun show(
    titleRes: Int,
    messageRes: Int,
    textOkRes: Int,
    onOkClicked: () -> Unit = { this@InfoDialog.childViewAs<SimpleDialog>(dialogInfo).hide() }
  ) {
    childViewAs<SimpleDialog>(dialogInfo).show()
    childViewAs<TextView>(dialogInfoTitle).text(titleRes)
    childViewAs<TextView>(dialogInfoMessage).text(messageRes)
    childViewAs<TextView>(dialogInfoTextOk).text(textOkRes)
    childViewAs<TextView>(dialogInfoTextOk).onClick(onOkClicked)
  }
  
  fun hide() {
    childViewAs<SimpleDialog>(dialogInfo).hide()
  }
  
  companion object {
    
    fun Screen.infoDialog() = viewAs<InfoDialog>()
    
    fun ViewGroup.infoDialog() = childViewAs<InfoDialog>()
    
    fun ViewGroup.InfoDialog(tagPrefix: String = "", block: InfoDialog.() -> Unit = {}) = withViewBuilder {
      val infoDialog = InfoDialog(context, tagPrefix)
      infoDialog.size(MatchParent, MatchParent)
      infoDialog.classNameTag()
      addView(infoDialog)
      infoDialog.apply(block)
    }
  }
}