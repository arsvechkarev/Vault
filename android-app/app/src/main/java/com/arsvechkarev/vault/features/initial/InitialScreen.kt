package com.arsvechkarev.vault.features.initial

import android.content.Context
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.Screens
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageLogoSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
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
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import navigation.BaseScreen

class InitialScreen : BaseScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      setBackgroundResource(R.drawable.bg_initial_screen)
      VerticalLayout(MatchParent, WrapContent) {
        layoutGravity(CENTER)
        gravity(CENTER)
        ImageView(ImageLogoSize, ImageLogoSize) {
          image(R.mipmap.ic_launcher)
          onClick { rotate() }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          margins(top = MarginNormal)
          text(R.string.text_welcome_to_vault)
          textSize(TextSizes.MainHeader)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          margin(MarginExtraLarge)
          gravity(CENTER)
          text(R.string.text_welcome_description)
          textSize(TextSizes.H3)
        }
      }
      ClickableButton(MatchParent, WrapContent) {
        margin(MarginNormal)
        layoutGravity(BOTTOM)
        text(R.string.text_create_master_password)
        onClick { appComponent.router.goForward(Screens.CreateMasterPasswordScreen) }
      }
    }
  }
}
