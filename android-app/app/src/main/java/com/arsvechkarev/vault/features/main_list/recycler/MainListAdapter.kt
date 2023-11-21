package com.arsvechkarev.vault.features.main_list.recycler

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.views.MaterialProgressBar
import com.arsvechkarev.vault.features.common.domain.setIconForTitle
import com.arsvechkarev.vault.features.common.model.Empty
import com.arsvechkarev.vault.features.common.model.EmptySearch
import com.arsvechkarev.vault.features.common.model.EntryItem
import com.arsvechkarev.vault.features.common.model.Loading
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.common.model.Title
import com.arsvechkarev.vault.features.common.model.Title.Type
import com.arsvechkarev.vault.recycler.BaseListAdapter
import com.arsvechkarev.vault.recycler.delegate
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.HorizontalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageNoEntriesSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageSizeBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.Size.IntSize
import viewdsl.backgroundCircle
import viewdsl.constraints
import viewdsl.gone
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
import viewdsl.visible

class MainListAdapter(
  private val onItemClick: (EntryItem) -> Unit,
  private val onImageLoadingFailed: () -> Unit
) : BaseListAdapter() {
  
  init {
    addDelegates(
      delegate<Title> {
        buildView {
          RootTextView(WrapContent, WrapContent, style = BoldTextView) {
            textSize(TextSizes.H4)
            margins(
              top = MarginLarge,
              bottom = MarginNormal,
              start = MarginNormal
            )
          }
        }
        onBind {
          (itemView as TextView).setText(when (item.type) {
            Type.FAVORITES -> R.string.text_favorites
            Type.PASSWORDS -> R.string.text_passwords
            Type.PLAIN_TEXTS -> R.string.text_plain_texts
          })
        }
      },
      delegate<PasswordItem> {
        buildView {
          RootConstraintLayout(MatchParent, WrapContent) {
            rippleBackground(Colors.Ripple)
            clipChildren = false
            paddingHorizontal(HorizontalMarginSmall)
            paddingVertical(MarginSmall)
            ImageView(ImageSizeBig, ImageSizeBig) {
              id(ItemPasswordEntryImage)
              margins(start = MarginNormal)
              constraints {
                topToTopOf(parent)
                startToStartOf(parent)
                bottomToBottomOf(parent)
              }
            }
            TextView(ZERO, WrapContent, style = BaseTextView) {
              id(ItemPasswordEntryTitle)
              setSingleLine()
              margins(start = MarginNormal, end = MarginNormal, bottom = MarginTiny / 2)
            }
            View(MatchParent, IntSize(1)) {
              id(ItemPasswordEntryVerticalGuideline)
            }
            TextView(ZERO, WrapContent, style = SecondaryTextView) {
              id(ItemPasswordEntrySubtitle)
              setSingleLine()
              margins(start = MarginNormal, end = MarginNormal, top = MarginTiny / 2)
              constraints {
                startToEndOf(ItemPasswordEntryImage)
                endToEndOf(parent)
                topToBottomOf(ItemPasswordEntryVerticalGuideline)
              }
            }
          }
        }
        onInitViewHolder {
          itemView.onClick { onItemClick(item) }
        }
        onBind {
          itemView.viewAs<ImageView>(ItemPasswordEntryImage).setIconForTitle(item.title,
            onImageLoadingFailed)
          itemView.viewAs<TextView>(ItemPasswordEntryTitle).text(item.title)
          if (item.username.isNotEmpty()) {
            itemView.viewAs<TextView>(ItemPasswordEntrySubtitle).visible()
            itemView.viewAs<TextView>(ItemPasswordEntrySubtitle).text(item.username)
            itemView.viewAs<TextView>(ItemPasswordEntryTitle).constraints {
              startToEndOf(ItemPasswordEntryImage)
              endToEndOf(parent)
              bottomToTopOf(ItemPasswordEntryVerticalGuideline)
            }
            itemView.viewAs<View>(ItemPasswordEntryVerticalGuideline).constraints {
              topToTopOf(parent)
              bottomToBottomOf(parent)
            }
          } else {
            itemView.viewAs<TextView>(ItemPasswordEntrySubtitle).gone()
            itemView.viewAs<TextView>(ItemPasswordEntryTitle).constraints {
              startToEndOf(ItemPasswordEntryImage)
              topToTopOf(parent)
              bottomToBottomOf(parent)
              endToEndOf(parent)
            }
            itemView.viewAs<View>(ItemPasswordEntryVerticalGuideline).constraints {
              bottomToBottomOf(parent)
            }
          }
        }
      },
      delegate<PlainTextItem> {
        buildView {
          RootHorizontalLayout(MatchParent, WrapContent) {
            rippleBackground(Colors.Ripple)
            paddingHorizontal(HorizontalMarginSmall)
            paddingVertical(MarginSmall)
            FrameLayout(IntSize(ImageSizeBig), IntSize(ImageSizeBig)) {
              layoutGravity(CENTER_VERTICAL)
              margins(start = MarginNormal, end = MarginNormal)
              View(MatchParent, MatchParent) {
                layoutGravity(CENTER)
                backgroundCircle(Colors.Accent)
              }
              ImageView(ImageSize, ImageSize) {
                layoutGravity(CENTER)
                image(R.drawable.ic_plain_text)
              }
            }
            TextView(WrapContent, WrapContent, style = BaseTextView) {
              id(ItemPlainTextTitle)
              maxLines = 2
              layoutGravity(CENTER)
            }
          }
        }
        onInitViewHolder {
          itemView.onClick { onItemClick(item) }
        }
        onBind {
          itemView.viewAs<TextView>(ItemPlainTextTitle).text(item.title)
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
            ImageView(ImageNoEntriesSize, ImageNoEntriesSize) {
              image(R.drawable.ic_lists)
              margin(MarginNormal)
            }
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              marginHorizontal(MarginNormal)
              textSize(TextSizes.H3)
              text(R.string.text_no_entries)
            }
            TextView(WrapContent, WrapContent, style = BaseTextView) {
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
      },
      delegate<EmptySearch> {
        buildView {
          RootTextView(MatchParent, MatchParent, style = BaseTextView) {
            gravity(CENTER)
            textSize(TextSizes.H3)
            text("No matching entries")
          }
        }
      }
    )
  }
  
  companion object {
    
    val ItemPasswordEntryImage = View.generateViewId()
    val ItemPasswordEntryTitle = View.generateViewId()
    val ItemPasswordEntryVerticalGuideline = View.generateViewId()
    val ItemPasswordEntrySubtitle = View.generateViewId()
    val ItemPlainTextTitle = View.generateViewId()
  }
}
