package com.arsvechkarev.vault.features.main_list.recycler

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity.CENTER
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.model.PasswordItem
import com.arsvechkarev.vault.core.views.MaterialProgressBar
import com.arsvechkarev.vault.features.common.setWebsiteIcon
import com.arsvechkarev.vault.recycler.BaseListAdapter
import com.arsvechkarev.vault.recycler.delegate
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageNoServicesSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ItemServiceInfoImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewbuilding.TypefaceSpan
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.gravity
import viewdsl.id
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.paddings
import viewdsl.rippleBackground
import viewdsl.text
import viewdsl.textSize
import viewdsl.viewAs

class MainListAdapter(
  private val onItemClick: (PasswordItem) -> Unit,
) : BaseListAdapter() {
  
  init {
    addDelegates(
      delegate<PasswordItem> {
        buildView {
          RootHorizontalLayout(MatchParent, WrapContent) {
            rippleBackground(Colors.Ripple)
            paddingHorizontal(HorizontalMarginSmall)
            paddingVertical(MarginSmall)
            ImageView(ItemServiceInfoImageSize, ItemServiceInfoImageSize) {
              id(ItemPasswordInfoImage)
              layoutGravity(CENTER)
              margins(start = MarginNormal, end = MarginNormal)
            }
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              id(ItemPasswordInfoTextPasswordName)
              layoutGravity(CENTER)
            }
          }
        }
        onInitViewHolder {
          itemView.onClick { onItemClick(item) }
        }
        onBind {
          itemView.viewAs<ImageView>(ItemPasswordInfoImage).setWebsiteIcon(item.websiteName)
          itemView.viewAs<TextView>(ItemPasswordInfoTextPasswordName).text(item.websiteName)
        }
      },
      delegate<Loading> {
        buildView {
          RootVerticalLayout {
            paddings(top = GradientDrawableHeight)
            gravity(CENTER)
            child<MaterialProgressBar>(ProgressBarSizeBig, ProgressBarSizeBig)
          }
        }
      },
      delegate<Empty> {
        buildView {
          RootVerticalLayout {
            marginHorizontal(MarginLarge)
            gravity(CENTER)
            ImageView(ImageNoServicesSize, ImageNoServicesSize) {
              image(R.drawable.ic_lists)
              margin(MarginNormal)
            }
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              marginHorizontal(MarginNormal)
              textSize(TextSizes.H3)
              text(R.string.text_no_entries)
            }
            TextView(WrapContent, WrapContent, style = Styles.BaseTextView) {
              textSize(TextSizes.H4)
              margin(MarginNormal)
              gravity(CENTER)
              val spannableString = SpannableString(context.getString(R.string.text_click_plus))
              val index = spannableString.indexOf('+')
              spannableString.setSpan(
                TypefaceSpan(Fonts.SegoeUiBold), index, index + 1, 0)
              spannableString.setSpan(RelativeSizeSpan(1.3f), index, index + 1, 0)
              text(spannableString)
            }
          }
        }
      }
    )
  }
  
  companion object {
    
    val ItemPasswordInfoImage = View.generateViewId()
    val ItemPasswordInfoTextPasswordName = View.generateViewId()
  }
}
