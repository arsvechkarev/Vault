package com.arsvechkarev.vault.features.initial_screen

import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.navigation.ViewScreen
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginDefault
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.margins
import com.arsvechkarev.vault.viewdsl.onClick
import com.arsvechkarev.vault.viewdsl.rotate
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize

class InitialScreen : ViewScreen() {
  
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout {
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(CENTER)
        gravity(CENTER)
        ImageView(ImageLogoSize, ImageLogoSize) {
          image(R.mipmap.ic_launcher)
          onClick { rotate() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginDefault)
          text(R.string.text_welcome_to_vault)
          textSize(TextSizes.H1)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margin(MarginBig)
          gravity(CENTER)
          text(R.string.text_welcome_description)
          textSize(TextSizes.H3)
        }
      }
      TextView(MatchParent, WrapContent, style = ClickableButton()) {
        margin(MarginDefault)
        layoutGravity(BOTTOM)
        text(R.string.text_create_master_password)
        onClick { navigator.goToCreatingMasterPasswordScreen() }
      }
    }
  }
}