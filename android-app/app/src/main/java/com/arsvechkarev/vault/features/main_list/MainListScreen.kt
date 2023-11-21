package com.arsvechkarev.vault.features.main_list

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.EntryTypeItemView
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior
import com.arsvechkarev.vault.core.views.behaviors.BottomSheetBehavior.Companion.asBottomSheet
import com.arsvechkarev.vault.core.views.menu.MenuItemModel
import com.arsvechkarev.vault.core.views.menu.MenuView
import com.arsvechkarev.vault.features.common.AppConstants.CONTENT_TYPE_UNKNOWN
import com.arsvechkarev.vault.features.common.AppConstants.MIME_TYPE_ALL
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.model.Empty
import com.arsvechkarev.vault.features.common.model.Loading
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectExportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListNews.LaunchSelectImportFileActivity
import com.arsvechkarev.vault.features.main_list.MainListNews.NotifyDatasetChanged
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnCloseMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeDialogHidden
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnEntryTypeSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnExportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnHideShareExportedFileDialog
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnImagesLoadingFailed
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnImportFileSelected
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnInit
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnListItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnMenuItemClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnOpenMenuClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnSearchClicked
import com.arsvechkarev.vault.features.main_list.MainListUiEvent.OnSearchTextChanged
import com.arsvechkarev.vault.features.main_list.MenuItemType.EXPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.IMPORT_PASSWORDS
import com.arsvechkarev.vault.features.main_list.MenuItemType.NEW_ENTRY
import com.arsvechkarev.vault.features.main_list.MenuItemType.SETTINGS
import com.arsvechkarev.vault.features.main_list.recycler.MainListAdapter
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginTiny
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.TitleTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import domain.CommonConstants.DEFAULT_EXPORT_FILENAME
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundColor
import viewdsl.backgroundTopRoundRect
import viewdsl.behavior
import viewdsl.circleRippleBackground
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.dp
import viewdsl.gone
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.invisible
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.paddings
import viewdsl.setTextSilently
import viewdsl.setupWith
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class MainListScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      id(MainListScreenRoot)
      backgroundColor(Colors.Background)
      clipChildren = false
      RecyclerView(MatchParent, MatchParent) {
        classNameTag()
        paddings(top = GradientDrawableHeight - MarginSmall, bottom = 80.dp)
        clipChildren = false
        clipToPadding = false
        setupWith(this@MainListScreen.adapter)
      }
      ImageView(MatchParent, WrapContent) {
        image(R.drawable.bg_gradient)
        scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
      }
      child<ConstraintLayout>(MatchParent, WrapContent) {
        margins(top = MarginNormal + StatusBarHeight)
        TextView(WrapContent, WrapContent, style = TitleTextView) {
          id(MainTitle)
          text(R.string.app_name)
          constraints {
            centeredWithin(parent)
          }
        }
        EditText(MatchParent, WrapContent) {
          id(EditTextSearch)
          invisible()
          setHint(R.string.text_search)
          setHintTextColor(Colors.TextSecondary)
          margins(start = MarginNormal - MarginTiny, end = ImageSize + MarginLarge + MarginSmall)
          addTextChangedListener(searchTextWatcher)
          constraints {
            centeredWithin(parent)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageSearchAction)
          image(R.drawable.ic_search)
          margins(end = MarginNormal)
          padding(IconPadding)
          circleRippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnSearchClicked) }
          constraints {
            topToTopOf(parent)
            endToEndOf(parent)
            bottomToBottomOf(parent)
          }
        }
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
          onSlideFractionChanged = { fraction ->
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
      InfoDialog()
      LoadingDialog()
    }
  }
  
  private val selectExportFileLauncher = coreComponent.activityResultWrapper
      .wrapCreateFileLauncher(this, CONTENT_TYPE_UNKNOWN) { uri ->
        store.tryDispatch(OnExportFileSelected(uri))
      }
  
  private val selectImportFileLauncher = coreComponent.activityResultWrapper
      .wrapGetFileLauncher(this@MainListScreen) { uri ->
        store.tryDispatch(OnImportFileSelected(uri))
      }
  
  private val store by viewModelStore { MainListStore(coreComponent) }
  
  private val adapter by lazy {
    MainListAdapter(
      onItemClick = { item -> store.tryDispatch(OnListItemClicked(item)) },
      onImageLoadingFailed = { store.tryDispatch(OnImagesLoadingFailed) }
    )
  }
  
  private val searchTextWatcher = BaseTextWatcher { store.tryDispatch(OnSearchTextChanged(it)) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  private fun render(state: MainListState) {
    if (state.data.isEmpty) {
      view(ImageSearchAction).gone()
    } else {
      view(ImageSearchAction).visible()
    }
    if (state.inSearchMode) {
      editText(EditTextSearch).setTextSilently(state.searchText, searchTextWatcher)
      imageView(ImageSearchAction).image(R.drawable.ic_cross)
      view(MainTitle).animateInvisible()
      view(EditTextSearch).animateVisible()
      requireView().post {
        viewAsNullable<EditText>(EditTextSearch)?.apply {
          requestFocus()
          requireContext().showKeyboard(this)
        }
      }
    } else {
      imageView(ImageSearchAction).image(R.drawable.ic_search)
      view(MainTitle).animateVisible()
      view(EditTextSearch).animateInvisible()
      view(EditTextSearch).clearFocus()
      requireContext().hideKeyboard()
    }
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
    if (state.showExportingFileDialog) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
    if (state.showShareExportedFileDialog) {
      infoDialog.showWithCancelAndProceedOption(
        titleRes = R.string.text_done,
        message = getString(R.string.text_export_successful),
        cancelTextRes = R.string.text_ok,
        proceedTextRes = R.string.text_export_share_file,
        showProceedAsError = false,
        onCancel = { store.tryDispatch(OnHideShareExportedFileDialog) },
        onProceed = { shareExportedFile(state.exportedFileUri) }
      )
    } else {
      infoDialog.hide()
    }
    adapter.submitList(state.data.getItems(
      successItems = { it },
      loadingItems = { listOf(Loading) },
      emptyItems = { listOf(Empty) }
    ))
  }
  
  @SuppressLint("NotifyDataSetChanged")
  private fun handleNews(news: MainListNews) {
    when (news) {
      NotifyDatasetChanged -> {
        adapter.notifyDataSetChanged()
      }
      is LaunchSelectExportFileActivity -> {
        selectExportFileLauncher.launch(DEFAULT_EXPORT_FILENAME)
      }
      LaunchSelectImportFileActivity -> {
        selectImportFileLauncher.launch(MIME_TYPE_ALL)
      }
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private fun shareExportedFile(exportedFileUri: Uri?) {
    if (exportedFileUri == null) {
      return
    }
    val shareIntent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_STREAM, exportedFileUri)
      type = CONTENT_TYPE_UNKNOWN
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.text_export_share)))
  }
  
  companion object {
    
    val MainListScreenRoot = View.generateViewId()
    val MainTitle = View.generateViewId()
    val EditTextSearch = View.generateViewId()
    val ImageSearchAction = View.generateViewId()
    val ChooseEntryTypeBottomSheet = View.generateViewId()
  }
}
