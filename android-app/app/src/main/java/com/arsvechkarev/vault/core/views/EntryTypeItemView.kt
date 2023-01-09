package com.arsvechkarev.vault.core.views

import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.ViewGroup
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSize
import com.arsvechkarev.vault.viewbuilding.Dimens.IconSizeBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.backgroundCircle
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.onClick
import viewdsl.paddings
import viewdsl.rippleBackground
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

fun ViewGroup.EntryTypeItemView(iconRes: Int, textRes: Int, onClick: () -> Unit) {
  withViewBuilder {
    HorizontalLayout(MatchParent, WrapContent) {
      paddings(start = MarginNormal + MarginTiny, top = MarginSmall, bottom = MarginSmall)
      rippleBackground(Colors.Ripple)
      onClick(onClick)
      FrameLayout(IntSize(IconSizeBig), IntSize(IconSizeBig)) {
        layoutGravity(CENTER_VERTICAL)
        View(MatchParent, MatchParent) {
          layoutGravity(CENTER)
          backgroundCircle(Colors.Accent)
        }
        ImageView(IconSize, IconSize) {
          layoutGravity(CENTER)
          image(iconRes)
        }
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        layoutGravity(CENTER_VERTICAL)
        text(textRes)
        textSize(TextSizes.H4)
        margin(MarginNormal)
      }
    }
  }
}
