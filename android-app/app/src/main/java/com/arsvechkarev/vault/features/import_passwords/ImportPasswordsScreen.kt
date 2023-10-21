package com.arsvechkarev.vault.features.import_passwords

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.arg
import com.arsvechkarev.vault.core.extensions.booleanArg
import com.arsvechkarev.vault.core.extensions.toReadablePath
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.features.common.AppConstants.MIME_TYPE_ALL
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.enterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Mode.ImportingPasswords
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnConfirmedImportClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideErrorDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideInfoDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHidePasswordEnteringDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnImportPasswordsClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnPasswordEntered
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedFile
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.constraints
import viewdsl.id
import viewdsl.margins
import viewdsl.onClick
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class ImportPasswordsScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    RootCoordinatorLayout {
      id(ImportPasswordsScreenRoot)
      child<ConstraintLayout>(MatchParent, MatchParent) {
        ImageView(WrapContent, WrapContent, style = Styles.ImageBack) {
          id(ImageBack)
          margins(start = MarginSmall, top = StatusBarHeight + MarginMedium)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            startToStartOf(parent)
            topToTopOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(Title)
          text(R.string.text_import_passwords)
          textSize(TextSizes.H1)
          margins(start = MarginNormal)
          constraints {
            startToEndOf(ImageBack)
            topToTopOf(ImageBack)
          }
        }
        VerticalLayout(MatchParent, WrapContent) {
          id(LayoutSelectFile)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          onClick { selectFileResultLauncher.launch(MIME_TYPE_ALL) }
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
        mode = ImportingPasswords(fromInitialScreen = !booleanArg(ASK_FOR_CONFIRMATION)),
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
    ImportPasswordsStore(coreComponent, arg(Uri::class), booleanArg(ASK_FOR_CONFIRMATION))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  private fun render(state: ImportPasswordsState) {
    textView(TextSelectFile).text(state.selectedFileUri.toReadablePath())
    if (state.showLoading) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
    if (state.showEnterPasswordDialog) {
      enterPasswordDialog.show()
    } else {
      enterPasswordDialog.hide()
    }
    when (state.infoDialog) {
      ImportPasswordsInfoDialog.CONFIRMATION -> {
        infoDialog.showWithCancelAndProceedOption(
          titleRes = R.string.text_confirmation,
          message = getString(R.string.text_confirm_import_passwords_message),
          proceedTextRes = R.string.text_confirm,
          onCancel = { store.tryDispatch(OnHideInfoDialog) },
          onProceed = { store.tryDispatch(OnConfirmedImportClicked) }
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
  
  companion object {
    
    const val ASK_FOR_CONFIRMATION = "ASK_FOR_CONFIRMATION"
    
    val ImportPasswordsScreenRoot = View.generateViewId()
    val ImageBack = View.generateViewId()
    val Title = View.generateViewId()
    val LayoutSelectFile = View.generateViewId()
    val TitleSelectFile = View.generateViewId()
    val TextSelectFile = View.generateViewId()
    val ButtonImportPasswords = View.generateViewId()
  }
}
