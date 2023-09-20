package com.arsvechkarev.vault.features.main_list

import android.content.Context
import android.graphics.Color
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EntryTypeItemView
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.vault.core.views.menu.MenuItemModel
import com.arsvechkarev.vault.core.views.menu.MenuView
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.model.Empty
import com.arsvechkarev.vault.features.common.model.Loading
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeDialogHidden
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnListItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MenuItemType.EXPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.IMPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.NEW_ENTRY
import com.arsvechkarev.vault.features.main_list.MenuItemType.SETTINGS
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundColor
import viewdsl.backgroundTopRoundRect
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.dp
import viewdsl.id
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.paddings
import viewdsl.setupWith
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class MainListScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      backgroundColor(Colors.Background)
      RecyclerView(MatchParent, MatchParent) {
        classNameTag()
        paddings(top = GradientDrawableHeight - MarginSmall, bottom = 80.dp)
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
          menuItemModel(R.drawable.ic_new_entry, R.string.text_new_entry, NEW_ENTRY),
        )
        onMenuOpenClick {
          store.tryDispatch(OnOpenMenuClicked)
        }
        onMenuCloseClick { store.tryDispatch(OnCloseMenuClicked) }
      }
      val shadowLayout = FrameLayout(MatchParent, MatchParent)
      VerticalLayout(MatchParent, WrapContent) {
        id(ChooseEntryTypeBottomSheet)
        backgroundTopRoundRect(MarginNormal, Colors.Dialog)
        behavior(BottomSheetBehavior().apply {
          onHide = { store.tryDispatch(OnEntryTypeDialogHidden) }
          onSlidePercentageChanged = { fraction ->
            val color = ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, fraction)
            shadowLayout.setBackgroundColor(color)
          }
        })
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          text(R.string.text_choose_entry_type)
          isClickable = true
          textSize(TextSizes.H2)
          margins(start = MarginNormal, top = MarginNormal, bottom = MarginLarge)
        }
        val entryItem: (EntryType, Int, Int) -> Unit = { entryType, iconRes, textRes ->
          EntryTypeItemView(iconRes, textRes) { store.tryDispatch(OnEntryTypeSelected(entryType)) }
        }
        entryItem(EntryType.PASSWORD, R.drawable.ic_lock, R.string.text_password)
        entryItem(EntryType.PLAIN_TEXT, R.drawable.ic_plain_text, R.string.text_plain_text)
      }
    }
  }
  
  private val store by viewModelStore { MainListStore(coreComponent) }
  
  private val adapter by lazy {
    MainListAdapter(
      onItemClick = { item -> store.tryDispatch(OnListItemClicked(item)) },
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
    if (state.showEntryTypeDialog) {
      view(ChooseEntryTypeBottomSheet).asBottomSheet.show()
    } else {
      view(ChooseEntryTypeBottomSheet).asBottomSheet.hide()
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
  
  companion object {
    
    val ChooseEntryTypeBottomSheet = View.generateViewId()
  }
}
