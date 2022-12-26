package com.arsvechkarev.vault.features.main_list

import android.content.Context
import android.view.Gravity.CENTER_HORIZONTAL
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.menu.MenuItemModel
import com.arsvechkarev.vault.core.views.menu.MenuView
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
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
import com.arsvechkarev.vault.features.main_list.recycler.Empty
import com.arsvechkarev.vault.features.main_list.recycler.Loading
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundColor
import viewdsl.classNameTag
import viewdsl.dp
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.paddings
import viewdsl.setupWith
import viewdsl.text
import viewdsl.withViewBuilder

class MainListScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      backgroundColor(Colors.Background)
      RecyclerView(MatchParent, MatchParent) {
        classNameTag()
        paddings(top = GradientDrawableHeight, bottom = 80.dp)
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
      child<MenuView>(MatchParent, MatchParent) {
        classNameTag()
        val menuItemModel: (Int, Int, MenuItemType) -> MenuItemModel = { iconRes, titleRes, itemType ->
          MenuItemModel(iconRes, titleRes) { store.tryDispatch(OnMenuItemClicked(itemType)) }
        }
        items(
          menuItemModel(R.drawable.ic_import, R.string.text_import_passwords, IMPORT_PASSWORDS),
          menuItemModel(R.drawable.ic_export, R.string.text_export_passwords, EXPORT_PASSWORDS),
          menuItemModel(R.drawable.ic_settings, R.string.text_settings, SETTINGS),
          menuItemModel(R.drawable.ic_new_password, R.string.text_new_password, NEW_PASSWORD),
        )
        onMenuOpenClick { store.tryDispatch(OnOpenMenuClicked) }
        onMenuCloseClick { store.tryDispatch(OnCloseMenuClicked) }
      }
    }
  }
  
  private val store by viewModelStore { MainListStore(coreComponent) }
  
  private val adapter by lazy {
    MainListAdapter(
      onItemClick = { item -> store.tryDispatch(OnPasswordItemClicked(item)) },
    )
  }
  
  override fun onInit() {
    store.subscribe(this, ::render)
    store.tryDispatch(OnInit)
  }
  
  private fun render(state: MainListState) {
    if (state.menuOpened) {
      viewAs<MenuView>().openMenu()
    } else {
      viewAs<MenuView>().closeMenu()
    }
    adapter.submitList(state.data.getItems(
      successItems = { it },
      loadingItems = { listOf(Loading) },
      emptyItems = { listOf(Empty) }
    ))
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
}
