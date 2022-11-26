package com.arsvechkarev.vault.features.export_passwords

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.di.appComponent
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.dialogs.CheckMasterPasswordDialog.Companion.CheckMasterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.CheckMasterPasswordDialog.Companion.checkMasterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsNews.TryExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnExportPasswordClicked
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnFilenameTextChanged
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnHideMasterPasswordCheckDialog
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnMasterPasswordCheckPassed
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsUiEvent.OnSelectedFolder
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.ImageBack
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
import viewdsl.textSize
import viewdsl.withViewBuilder


class ExportPasswordsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    RootCoordinatorLayout {
      child<ConstraintLayout>(MatchParent, MatchParent) {
        HorizontalLayout(MatchParent, WrapContent) {
          id(ToolbarId)
          margins(top = StatusBarHeight + MarginNormal)
          constraints {
            topToTopOf(parent)
          }
          ImageView(WrapContent, WrapContent, style = ImageBack) {
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
          id(LayoutFolderName)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          onClick { selectFolder() }
          constraints {
            topToTopOf(parent)
          }
          TextView(WrapContent, WrapContent, style = AccentTextView) {
            id(TitleFolderName)
            text(R.string.text_folder)
          }
          TextView(MatchParent, WrapContent, style = SecondaryTextView) {
            id(TextFolderPath)
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
            topToBottomOf(LayoutFolderName)
            startToStartOf(parent)
          }
        }
        EditText(MatchParent, WrapContent, style = BaseEditText(hint = R.string.hint_filename)) {
          id(EditTextLogin)
          margins(start = MarginNormal, end = MarginNormal)
          onTextChanged { text -> store.tryDispatch(OnFilenameTextChanged(text)) }
          constraints {
            startToStartOf(parent)
            topToBottomOf(TitleFilename)
          }
        }
        TextView(MatchParent, WrapContent, style = Button()) {
          id(ButtonContinue)
          margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
          constraints {
            bottomToBottomOf(parent)
          }
          text(R.string.text_export_passwords)
          onClick { store.tryDispatch(OnExportPasswordClicked) }
          //        onClick { continueWithCreating() }
        }
      }
      CheckMasterPasswordDialog(
        onDialogClosed = { store.tryDispatch(OnHideMasterPasswordCheckDialog) },
        onCheckSuccessful = { store.tryDispatch(OnMasterPasswordCheckPassed) }
      )
      InfoDialog()
      LoadingDialog()
    }
  }
  
  
  private val store by viewModelStore { ExportPasswordsStore(appComponent) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
  }
  
  private fun render(state: ExportPasswordsState) {
    textView(TextFolderPath).text(getFolderPath(state.folderPath))
    when (state.dialogType) {
      ExportPasswordsDialogType.CHECKING_MASTER_PASSWORD -> {
        checkMasterPasswordDialog.show()
      }
      ExportPasswordsDialogType.LOADING -> {
        checkMasterPasswordDialog.hide()
        loadingDialog.show()
      }
      ExportPasswordsDialogType.SUCCESS_EXPORT -> {
        loadingDialog.show()
        //        infoDialog.showWithOkOption(
        //
        //        )
      }
      null -> {
        checkMasterPasswordDialog.hide()
        infoDialog.hide()
        loadingDialog.hide()
      }
    }
  }
  
  private fun getFolderPath(folderPath: String): String {
    if (folderPath.isEmpty()) {
      return getString(R.string.text_select_folder)
    }
    return folderPath.removePrefix("content://")
  }
  
  private fun handleNews(news: ExportPasswordsNews) {
    when (news) {
      is TryExportPasswords -> {
        println("qqqq: fileUri=${news.fileUri}, folder=${news.folderPath}, file=${news.filename}")
        tryExportPasswords(news)
      }
    }
  }
  
  private fun tryExportPasswords(news: TryExportPasswords) {
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "content/unknown"
      putExtra(Intent.EXTRA_TITLE, news.filename)
      putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.decode(news.fileUri))
    }
    startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
  }
  
  private fun selectFolder() {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
      addCategory(Intent.CATEGORY_DEFAULT)
    }
    val chooser = Intent.createChooser(intent, getString(R.string.text_select_folder))
    startActivityForResult(chooser, SELECT_FOLDER_REQUEST_CODE)
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      SELECT_FOLDER_REQUEST_CODE -> {
        val folderPath = data?.data ?: return
        store.tryDispatch(OnSelectedFolder(folderPath.toString()))
      }
      CREATE_FILE_REQUEST_CODE -> {
        
        println(data?.data) ?: return
      }
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private companion object {
    
    const val SELECT_FOLDER_REQUEST_CODE = 3762
    const val CREATE_FILE_REQUEST_CODE = 9846
    
    val ToolbarId = View.generateViewId()
    val TitleFolderName = View.generateViewId()
    val LayoutFolderName = View.generateViewId()
    val TitleFilename = View.generateViewId()
    val ButtonContinue = View.generateViewId()
    val TextFolderPath = View.generateViewId()
    val EditTextLogin = View.generateViewId()
  }
}
