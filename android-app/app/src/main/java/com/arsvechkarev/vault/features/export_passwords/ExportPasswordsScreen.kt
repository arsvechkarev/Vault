package com.arsvechkarev.vault.features.export_passwords

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsNews.TryExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnExportPasswordClicked
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnFileForPasswordsExportCreated
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnFilenameTextChanged
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideViewExportedFileDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnSelectedFolder
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.gravity
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onTextChanged
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class ExportPasswordsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    RootCoordinatorLayout {
      id(ExportPasswordsScreenRoot)
      child<ConstraintLayout>(MatchParent, MatchParent) {
        HorizontalLayout(MatchParent, WrapContent) {
          id(Toolbar)
          margins(top = StatusBarHeight + MarginNormal)
          constraints {
            topToTopOf(parent)
          }
          ImageView(WrapContent, WrapContent, style = IconBack) {
            margins(start = MarginNormal, end = MarginNormal)
            gravity(Gravity.CENTER_VERTICAL)
            onClick { store.tryDispatch(OnBackPressed) }
          }
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            layoutGravity(Gravity.CENTER)
            text(R.string.text_export_passwords)
            textSize(TextSizes.H1)
          }
        }
        VerticalLayout(MatchParent, WrapContent) {
          id(LayoutFolder)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          onClick { selectFolderResultLauncher.launch(null) }
          constraints {
            topToTopOf(parent)
          }
          TextView(WrapContent, WrapContent, style = AccentTextView) {
            id(TitleFolder)
            text(R.string.text_folder)
          }
          TextView(MatchParent, WrapContent, style = SecondaryTextView) {
            id(TextFolder)
            textSize(TextSizes.H4)
            text(R.string.text_select_folder)
            margins(top = MarginSmall, end = MarginNormal)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleFilename)
          text(R.string.text_filename)
          margins(start = MarginNormal, top = MarginLarge)
          constraints {
            topToBottomOf(LayoutFolder)
            startToStartOf(parent)
          }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText(hint = R.string.hint_filename)) {
          id(EditTextFilename)
          margins(start = MarginNormal, end = MarginNormal)
          onTextChanged { text -> store.tryDispatch(OnFilenameTextChanged(text)) }
          constraints {
            startToStartOf(parent)
            topToBottomOf(TitleFilename)
          }
        }
        TextView(MatchParent, WrapContent, style = Button()) {
          id(ButtonExportPasswords)
          margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
          constraints {
            bottomToBottomOf(parent)
          }
          text(R.string.text_export_passwords)
          onClick { store.tryDispatch(OnExportPasswordClicked) }
        }
      }
      InfoDialog()
    }
  }
  
  private val selectFolderResultLauncher = coreComponent.activityResultSubstitutor
      .wrapSelectFolderLauncher(this) { uri ->
        store.tryDispatch(OnSelectedFolder(uri.toString()))
      }
  
  private val createFileResultLauncher = coreComponent.activityResultSubstitutor
      .wrapCreateFileLauncher(this, CONTENT_TYPE) { uri ->
        store.tryDispatch(OnFileForPasswordsExportCreated(uri))
      }
  
  private val store by viewModelStore { ExportPasswordsStore(coreComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
  }
  
  private fun render(state: ExportPasswordsState) {
    renderTexts(state)
    renderErrors(state)
    renderDialogs(state)
  }
  
  private fun renderTexts(state: ExportPasswordsState) {
    val folderText = if (state.folderPath.isEmpty()) {
      getString(R.string.text_select_folder)
    } else {
      state.folderPath.removePrefix(CONTENT_PREFIX)
    }
    textView(TextFolder).text(folderText)
    editText(EditTextFilename).apply {
      if (state.filename != text.toString()) {
        setText(state.filename)
      }
    }
  }
  
  private fun renderErrors(state: ExportPasswordsState) {
    if (state.showSelectFolderError) {
      textView(TitleFolder).textColor(Colors.Error)
      textView(TitleFolder).text(R.string.text_error_folder_is_not_selected)
    } else {
      textView(TitleFolder).textColor(Colors.Accent)
      textView(TitleFolder).text(R.string.text_folder)
    }
    if (state.showEnterFilenameError) {
      textView(TitleFilename).textColor(Colors.Error)
      textView(TitleFilename).text(R.string.text_error_filename_is_empty)
    } else {
      textView(TitleFilename).textColor(Colors.Accent)
      textView(TitleFilename).text(R.string.text_filename)
    }
  }
  
  private fun renderDialogs(state: ExportPasswordsState) {
    if (state.showSuccessDialog) {
      infoDialog.showWithOkOption(
        titleRes = R.string.text_done,
        messageRes = R.string.text_export_successful,
        textPositiveRes = R.string.text_export_share_file,
        onCancel = { store.tryDispatch(OnHideViewExportedFileDialog) },
        onOkClicked = { shareExportedFile(state.exportedFileUri) }
      )
    } else {
      infoDialog.hide()
    }
  }
  
  private fun handleNews(news: ExportPasswordsNews) {
    when (news) {
      is TryExportPasswords -> createFileResultLauncher.launch(news.filename)
    }
  }
  
  private fun shareExportedFile(filePath: Uri?) {
    if (filePath == null) {
      return
    }
    val shareIntent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_STREAM, filePath)
      type = CONTENT_TYPE
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.text_export_share)))
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  companion object {
    
    const val CONTENT_TYPE = "content/unknown"
    const val CONTENT_PREFIX = "content://"
    
    val ExportPasswordsScreenRoot = View.generateViewId()
    val Toolbar = View.generateViewId()
    val LayoutFolder = View.generateViewId()
    val TitleFolder = View.generateViewId()
    val TextFolder = View.generateViewId()
    val TitleFilename = View.generateViewId()
    val EditTextFilename = View.generateViewId()
    val ButtonExportPasswords = View.generateViewId()
  }
}
