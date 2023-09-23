package com.arsvechkarev.vault.features.import_passwords

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import buisnesslogic.IMPORT_CONTENT_TYPE
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.booleanArg
import com.arsvechkarev.vault.core.extensions.nullableArg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.enterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.IMPORTING_PASSWORDS
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnConfirmedImportClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideErrorDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideInfoDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHidePasswordEnteringDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideSuccessDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnImportPasswordsClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnPasswordEntered
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedFile
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
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
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder
import java.io.File
import java.net.URLDecoder

class ImportPasswordsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    RootCoordinatorLayout {
      id(ImportPasswordsScreenRoot)
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
            text(R.string.text_import_passwords)
            textSize(TextSizes.H1)
          }
        }
        VerticalLayout(MatchParent, WrapContent) {
          id(LayoutSelectFile)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          onClick { selectFileResultLauncher.launch(IMPORT_CONTENT_TYPE) }
          constraints {
            topToTopOf(parent)
          }
          TextView(WrapContent, WrapContent, style = AccentTextView) {
            id(TitleSelectFile)
            text(R.string.text_file)
          }
          TextView(MatchParent, WrapContent, style = SecondaryTextView) {
            id(TextSelectFile)
            textSize(TextSizes.H4)
            text(R.string.text_select_file)
            margins(top = MarginSmall, end = MarginNormal)
          }
        }
        TextView(MatchParent, WrapContent, style = Button()) {
          id(ButtonImportPasswords)
          margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
          constraints {
            bottomToBottomOf(parent)
          }
          text(R.string.text_import_passwords)
          onClick { store.tryDispatch(OnImportPasswordsClicked) }
        }
      }
      EnterPasswordDialog(
        mode = IMPORTING_PASSWORDS,
        onDialogClosed = { store.tryDispatch(OnHidePasswordEnteringDialog) },
        onPasswordEntered = { store.tryDispatch(OnPasswordEntered(it)) }
      )
      InfoDialog()
      LoadingDialog()
    }
  }
  
  private val selectFileResultLauncher = coreComponent.activityResultWrapper
      .wrapGetFileLauncher(this) { uri -> store.tryDispatch(OnSelectedFile(uri)) }
  
  private val store by viewModelStore {
    ImportPasswordsStore(coreComponent, nullableArg(Uri::class), booleanArg(ASK_FOR_CONFIRMATION))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  private fun render(state: ImportPasswordsState) {
    renderText(state)
    renderError(state)
    renderDialogs(state)
  }
  
  private fun renderText(state: ImportPasswordsState) {
    if (state.selectedFileUri == null) {
      textView(TextSelectFile).text(R.string.text_select_file)
    } else {
      textView(TextSelectFile).text(getReadablePath(state.selectedFileUri))
    }
  }
  
  private fun renderError(state: ImportPasswordsState) {
    if (state.showSelectFileError) {
      textView(TitleSelectFile).textColor(Colors.Error)
      textView(TitleSelectFile).text(R.string.text_error_file_is_not_selected)
    } else {
      textView(TitleSelectFile).textColor(Colors.Accent)
      textView(TitleSelectFile).text(R.string.text_file)
    }
  }
  
  private fun renderDialogs(state: ImportPasswordsState) {
    if (state.showLoading) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
    if (state.showEnteringPassword) {
      enterPasswordDialog.show()
    } else {
      enterPasswordDialog.hide()
    }
    when (state.infoDialog) {
      // TODO (9/22/23): Not show this dialog if on start "Import passwords from vault was selected"
      ImportPasswordsInfoDialog.CONFIRMATION -> {
        infoDialog.showWithCancelAndProceedOption(
          titleRes = R.string.text_confirmation,
          messageRes = getString(R.string.text_confirm_import_passwords_message),
          proceedTextRes = R.string.text_confirm,
          onCancel = { store.tryDispatch(OnHideInfoDialog) },
          onProceed = { store.tryDispatch(OnConfirmedImportClicked) }
        )
      }
      
      ImportPasswordsInfoDialog.SUCCESS -> {
        infoDialog.showWithOkOption(
          titleRes = R.string.text_success,
          messageRes = R.string.text_import_successful,
          textPositiveRes = R.string.text_ok,
          onCancel = { store.tryDispatch(OnHideSuccessDialog) },
          onOkClicked = { store.tryDispatch(OnHideSuccessDialog) }
        )
      }
      
      ImportPasswordsInfoDialog.FAILURE -> {
        infoDialog.showWithOkOption(
          titleRes = R.string.text_error,
          messageRes = R.string.text_error_import_message,
          textPositiveRes = R.string.text_ok,
          onCancel = { store.tryDispatch(OnHideErrorDialog) },
          onOkClicked = { store.tryDispatch(OnHideErrorDialog) }
        )
      }
      
      null -> infoDialog.hide()
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private fun getReadablePath(uri: Uri): String {
    val uriString = uri.toString()
    val internalStorageBasePath = if (File("/storage/emulated/0").exists()) {
      "/storage/emulated/0"
    } else {
      Environment.getExternalStorageDirectory().absolutePath.trimEnd('/')
    }
    val filePath = uriString.substring(uriString.lastIndexOf("/") + 1)
    return internalStorageBasePath + "/" + URLDecoder.decode(filePath, "UTF-8")
  }
  
  companion object {
    
    const val ASK_FOR_CONFIRMATION = "ASK_FOR_CONFIRMATION"
    
    val ImportPasswordsScreenRoot = View.generateViewId()
    val Toolbar = View.generateViewId()
    val LayoutSelectFile = View.generateViewId()
    val TitleSelectFile = View.generateViewId()
    val TextSelectFile = View.generateViewId()
    val ButtonImportPasswords = View.generateViewId()
  }
}
