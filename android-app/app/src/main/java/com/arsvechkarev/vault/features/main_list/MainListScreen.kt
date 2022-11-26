package com.arsvechkarev.vault.features.main_list

import android.content.Context
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.Gravity.CENTER
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.VaultApplication.Companion.AppMainCoroutineScope
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.extensions.ifTrue
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnPasswordItemClicked
import com.arsvechkarev.vault.features.main_list.MenuItemType.EXPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.IMPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.NEW_PASSWORD
import com.arsvechkarev.vault.features.main_list.MenuItemType.SETTINGS
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageNoServicesSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import com.arsvechkarev.vault.views.MaterialProgressBar
import com.arsvechkarev.vault.views.menu.MenuItem
import com.arsvechkarev.vault.views.menu.MenuView
import kotlinx.coroutines.launch
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.addView
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundColor
import viewdsl.classNameTag
import viewdsl.dp
import viewdsl.gravity
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.paddings
import viewdsl.retrieveDrawable
import viewdsl.setupWith
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class MainListScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      backgroundColor(Colors.Background)
      RecyclerView(MatchParent, WrapContent) {
        classNameTag()
        val drawable = context.retrieveDrawable(R.drawable.bg_gradient)
        val gradientHeight = drawable.intrinsicHeight / 1.4
        paddings(
          top = gradientHeight.toInt(),
          bottom = 80.dp
        )
        clipToPadding = false
        setupWith(this@MainListScreen.adapter)
      }
      ImageView(MatchParent, WrapContent) {
        image(R.drawable.bg_gradient)
        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
      }
      TextView(WrapContent, WrapContent, style = TitleTextView) {
        margins(top = MarginNormal + StatusBarHeight)
        text(R.string.app_name)
        layoutGravity(CENTER_HORIZONTAL)
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutLoading)
        invisible()
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
        val menuItem: (Int, Int, MenuItemType) -> MenuItem = { iconRes, titleRes, itemType ->
          MenuItem(iconRes, titleRes) { store.tryDispatch(OnMenuItemClicked(itemType)) }
        }
        items(
          menuItem(R.drawable.ic_import, R.string.text_import_passwords, IMPORT_PASSWORDS),
          menuItem(R.drawable.ic_export, R.string.text_export_passwords, EXPORT_PASSWORDS),
          menuItem(R.drawable.ic_settings, R.string.text_settings, SETTINGS),
          menuItem(R.drawable.ic_new_password, R.string.text_new_password, NEW_PASSWORD),
        )
        onMenuOpenClick { store.tryDispatch(OnOpenMenuClicked) }
        onMenuCloseClick { store.tryDispatch(OnCloseMenuClicked) }
      }
    }
  }
  
  private val store by viewModelStore { MainListStore(appComponent) }
  
  private val adapter by lazy {
    MainListAdapter(
      onItemClick = { item -> store.tryDispatch(OnPasswordItemClicked(item)) },
    )
  }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  override fun onAppearedOnScreen() {
    AppMainCoroutineScope.launch { store.dispatch(OnInit) }
  }
  
  private fun render(state: MainListState) {
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
