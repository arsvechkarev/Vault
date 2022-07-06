package com.arsvechkarev.vault.features.services_list.presentation

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.END
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.ifTrue
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.settings.SettingsScreenSingleEvents
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.FabSize
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageNoServicesSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Dimens.VerticalMarginSmall
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.vault.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.vault.viewdsl.addView
import com.arsvechkarev.vault.viewdsl.animateInvisible
import com.arsvechkarev.vault.viewdsl.animateVisible
import com.arsvechkarev.vault.viewdsl.backgroundColor
import com.arsvechkarev.vault.viewdsl.behavior
import com.arsvechkarev.vault.viewdsl.circleRippleBackground
import com.arsvechkarev.vault.viewdsl.classNameTag
import com.arsvechkarev.vault.viewdsl.gone
import com.arsvechkarev.vault.viewdsl.gravity
import com.arsvechkarev.vault.viewdsl.id
import com.arsvechkarev.vault.viewdsl.image
import com.arsvechkarev.vault.viewdsl.invisible
import com.arsvechkarev.vault.viewdsl.layoutGravity
import com.arsvechkarev.vault.viewdsl.margin
import com.arsvechkarev.vault.viewdsl.marginHorizontal
import com.arsvechkarev.vault.viewdsl.padding
import com.arsvechkarev.vault.viewdsl.paddings
import com.arsvechkarev.vault.viewdsl.rippleBackground
import com.arsvechkarev.vault.viewdsl.setupWith
import com.arsvechkarev.vault.viewdsl.size
import com.arsvechkarev.vault.viewdsl.tag
import com.arsvechkarev.vault.viewdsl.text
import com.arsvechkarev.vault.viewdsl.textSize
import com.arsvechkarev.vault.viewdsl.visible
import com.arsvechkarev.vault.viewdsl.withViewBuilder
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.behaviors.HeaderBehavior
import com.arsvechkarev.vault.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.vault.views.behaviors.ViewUnderHeaderBehavior
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.views.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.views.dialogs.LoadingDialog
import com.arsvechkarev.vault.views.dialogs.loadingDialog
import navigation.BaseScreen

class ServicesListScreen : BaseScreen(), MviView<ServicesListState, SettingsScreenSingleEvents> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
    RootCoordinatorLayout {
      backgroundColor(Colors.Background)
      FrameLayout(MatchParent, WrapContent) {
        behavior(HeaderBehavior())
        paddings(top = VerticalMarginSmall + StatusBarHeight, bottom = MarginSmall,
          start = MarginNormal, end = MarginNormal)
        TextView(MatchParent, WrapContent, style = TitleTextView) {
          text(context.getString(R.string.text_passwords))
          layoutGravity(CENTER_VERTICAL)
        }
        ImageView(WrapContent, WrapContent) {
          id(R.drawable.ic_settings)
          gone()
          image(R.drawable.ic_settings)
          layoutGravity(CENTER_VERTICAL or END)
          padding(IconPadding)
          circleRippleBackground()
          //          onClick { presenter.applyAction(OnSettingsClicked) }
        }
      }
      RecyclerView(MatchParent, WrapContent) {
        classNameTag()
        behavior(ScrollingRecyclerBehavior())
        setupWith(this@ServicesListScreen.adapter)
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutLoading)
        invisible()
        behavior(viewUnderHeaderBehavior)
        layoutGravity(CENTER)
        gravity(CENTER)
        addView {
          MaterialProgressBar(context).apply {
            size(ProgressBarSizeBig, ProgressBarSizeBig)
          }
        }
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutNoPasswords)
        invisible()
        marginHorizontal(MarginLarge)
        behavior(viewUnderHeaderBehavior)
        layoutGravity(CENTER)
        gravity(CENTER)
        ImageView(ImageNoServicesSize, ImageNoServicesSize) {
          image(R.drawable.ic_lists)
          margin(MarginNormal)
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          marginHorizontal(MarginNormal)
          textSize(TextSizes.H3)
          text(R.string.text_no_passwords)
        }
        TextView(WrapContent, WrapContent, style = BaseTextView) {
          textSize(TextSizes.H4)
          margin(MarginNormal)
          gravity(CENTER)
          val spannableString = SpannableString(context.getString(R.string.text_click_plus))
          val index = spannableString.indexOf('+')
          spannableString.setSpan(TypefaceSpan(Fonts.SegoeUiBold), index, index + 1, 0)
          spannableString.setSpan(RelativeSizeSpan(1.3f), index, index + 1, 0)
          text(spannableString)
        }
      }
      ImageView(FabSize, FabSize) {
        margin(MarginNormal)
        padding(MarginSmall)
        image(R.drawable.ic_plus)
        layoutGravity(BOTTOM or END)
        rippleBackground(Colors.Ripple, Colors.Accent, cornerRadius = FabSize)
        //        onClick { presenter.applyAction(OnFabClicked) }
      }
      InfoDialog()
      LoadingDialog()
    }
  }
  
  private val adapter by lazy {
    ServicesListAdapter(
      onItemClick = {/* presenter.applyAction(OnServiceItemClicked(it))*/ },
      onItemLongClick = { /*presenter.applyAction(OnServiceItemLongClicked(it)) */ }
    )
  }
  
  override fun render(state: ServicesListState) {
    if (state.showSettingsIcon) {
      imageView(R.drawable.ic_settings).visible()
    }
    state.result?.handle {
      onLoading { showView(view(LayoutLoading)) }
      onEmpty { showView(view(LayoutNoPasswords)) }
      onSuccess { list ->
        showView(viewAs<RecyclerView>())
        adapter.submitList(list)
      }
    }
    val deleteDialog = state.deleteDialog
    if (deleteDialog != null) {
      infoDialog.showWithDeleteAndCancelOption(
        R.string.text_delete_service, getDeleteMessageText(deleteDialog.serviceModel.serviceName),
        onDeleteClicked = {/* presenter.applyAction(OnAgreeToDeleteServiceClicked)*/ }
      )
      //      infoDialog.onHide = { presenter.applyAction(HideDeleteDialog) }
    } else {
      infoDialog.hide()
    }
    if (state.showDeletionLoadingDialog) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
  }
  
  private fun showView(layout: View) {
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutNoPasswords).ifTrue({ it !== layout }, { animateInvisible() })
    layout.animateVisible()
  }
  
  private companion object {
    
    const val LayoutLoading = "LayoutLoading"
    const val LayoutNoPasswords = "LayoutNoPasswords"
  }
}