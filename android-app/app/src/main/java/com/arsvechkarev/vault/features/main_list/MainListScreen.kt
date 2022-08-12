package com.arsvechkarev.vault.features.main_list

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_VERTICAL
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.VaultApplication.Companion.AppMainCoroutineScope
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.extensions.ifTrue
import com.arsvechkarev.vault.core.extensions.moxyStore
import com.arsvechkarev.vault.core.mvi.MviView
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnPasswordItemClicked
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
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.behaviors.HeaderBehavior
import com.arsvechkarev.vault.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.vault.views.behaviors.ViewUnderHeaderBehavior
import com.arsvechkarev.vault.views.menu.MenuView
import kotlinx.coroutines.launch
import navigation.BaseScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.addView
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.paddings
import viewdsl.setupWith
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class MainListScreen : BaseScreen(), MviView<MainListState, Nothing> {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
    RootCoordinatorLayout {
      clipChildren = false
      FrameLayout(MatchParent, WrapContent) {
        behavior(HeaderBehavior())
        paddings(top = VerticalMarginSmall + StatusBarHeight, bottom = MarginSmall,
          start = MarginNormal, end = MarginNormal)
        TextView(MatchParent, WrapContent, style = TitleTextView) {
          text(context.getString(R.string.text_passwords))
          layoutGravity(CENTER_VERTICAL)
        }
      }
      RecyclerView(MatchParent, WrapContent) {
        classNameTag()
        behavior(ScrollingRecyclerBehavior())
        setupWith(this@MainListScreen.adapter)
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
      child<MenuView>(MatchParent, MatchParent) {
        classNameTag()
        onMenuOpenClick { store.tryDispatch(OnOpenMenuClicked) }
        onMenuCloseClick { store.tryDispatch(OnCloseMenuClicked) }
      }
    }
  }
  
  private val store by moxyStore { MainListStore(appComponent) }
  
  private val adapter by lazy {
    MainListAdapter(
      onItemClick = { item -> store.tryDispatch(OnPasswordItemClicked(item)) },
    )
  }
  
  override fun onInit() {
    AppMainCoroutineScope.launch { store.dispatch(OnInit) }
  }
  
  override fun render(state: MainListState) {
    if (state.menuOpened) {
      viewAs<MenuView>().openMenu()
    } else {
      viewAs<MenuView>().closeMenu()
    }
    state.data?.handle {
      onLoading { showView(view(LayoutLoading)) }
      onEmpty { showView(view(LayoutNoPasswords)) }
      onSuccess { list ->
        showView(viewAs<RecyclerView>())
        adapter.submitList(list)
      }
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
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