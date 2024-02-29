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
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnClearKeyFileClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnConfirmedImportClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideErrorDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHideInfoDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnHidePasswordEnteringDialog
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnImportPasswordsClicked
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnPasswordEntered
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsUiEvent.OnSelectedPasswordsFile
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
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
import viewdsl.Size.Companion.ZERO
import viewdsl.circleRippleBackground
import viewdsl.constraints
import viewdsl.id
import viewdsl.image
import viewdsl.isVisible
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
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
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleSelectPasswordsFile)
          text(R.string.text_passwords_file)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        TextView(MatchParent, WrapContent, style = SecondaryTextView) {
          id(TextSelectPasswordsFile)
          textSize(TextSizes.H4)
          margins(start = MarginNormal, top = MarginSmall, end = MarginNormal)
          constraints {
            topToBottomOf(TitleSelectPasswordsFile)
            startToStartOf(parent)
            endToEndOf(parent)
          }
        }
        View(MatchParent, ZERO) {
          id(ViewSelectPasswordsFile)
          onClick { selectPasswordsFileResultLauncher.launch(MIME_TYPE_ALL) }
          constraints {
            topToTopOf(TitleSelectPasswordsFile)
            bottomToBottomOf(TextSelectPasswordsFile)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleSelectKeyFile)
          text(R.string.text_key_file)
          margins(top = MarginLarge)
          constraints {
            topToBottomOf(TextSelectPasswordsFile)
            startToStartOf(TextSelectPasswordsFile)
          }
        }
        TextView(ZERO, WrapContent, style = SecondaryTextView) {
          id(TextSelectKeyFile)
          text(R.string.text_key_file_stub)
          textSize(TextSizes.H4)
          margins(start = MarginNormal, top = MarginSmall, end = MarginNormal)
          constraints {
            topToBottomOf(TitleSelectKeyFile)
            startToStartOf(parent)
            endToStartOf(ImageClearKeyFile)
          }
        }
        View(MatchParent, ZERO) {
          id(ViewSelectKeyFile)
          onClick { selectKeyFileResultLauncher.launch(MIME_TYPE_ALL) }
          constraints {
            topToTopOf(TitleSelectKeyFile)
            bottomToBottomOf(TextSelectKeyFile)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageClearKeyFile)
          image(R.drawable.ic_cross)
          margins(end = MarginNormal)
          padding(IconPadding)
          circleRippleBackground(Colors.Ripple)
          onClick { store.tryDispatch(OnClearKeyFileClicked) }
          constraints {
            topToTopOf(TextSelectKeyFile)
            endToEndOf(parent)
            bottomToBottomOf(TextSelectKeyFile)
          }
        }
        TextView(MatchParent, WrapContent, style = Button()) {
          id(ButtonImportPasswords)
          margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
          text(R.string.text_import_passwords)
          onClick { store.tryDispatch(OnImportPasswordsClicked) }
          constraints {
            bottomToBottomOf(parent)
          }
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
  
  private val selectPasswordsFileResultLauncher = coreComponent.activityResultWrapper
      .wrapSelectPasswordsFileLauncher(this) { uri -> store.tryDispatch(OnSelectedPasswordsFile(uri)) }
  
  private val selectKeyFileResultLauncher = coreComponent.activityResultWrapper
      .wrapSelectKeyFileLauncher(this) { uri -> store.tryDispatch(OnSelectedKeyFile(uri)) }
  
  private val store by viewModelStore {
    ImportPasswordsStore(coreComponent, arg(Uri::class), booleanArg(ASK_FOR_CONFIRMATION))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render)
  }
  
  private fun render(state: ImportPasswordsState) {
    textView(TextSelectPasswordsFile).text(state.passwordsFileUri.toReadablePath())
    state.keyFileUri?.toReadablePath()?.let { textView(TextSelectKeyFile).text(it) }
        ?: textView(TextSelectKeyFile).text(R.string.text_key_file_stub)
    view(ImageClearKeyFile).isVisible = state.keyFileUri != null
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
    val ViewSelectPasswordsFile = View.generateViewId()
    val TitleSelectPasswordsFile = View.generateViewId()
    val TextSelectPasswordsFile = View.generateViewId()
    val ViewSelectKeyFile = View.generateViewId()
    val ImageClearKeyFile = View.generateViewId()
    val TitleSelectKeyFile = View.generateViewId()
    val TextSelectKeyFile = View.generateViewId()
    val ButtonImportPasswords = View.generateViewId()
  }
}
