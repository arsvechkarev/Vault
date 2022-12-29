package com.arsvechkarev.vault.features.info

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.arg
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.Snackbar
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.setWebsiteIcon
import com.arsvechkarev.vault.features.info.InfoScreen.ImageType.COPY
import com.arsvechkarev.vault.features.info.InfoScreen.ImageType.OPEN_IN_NEW
import com.arsvechkarev.vault.features.info.InfoScreenNews.SetLogin
import com.arsvechkarev.vault.features.info.InfoScreenNews.SetNotes
import com.arsvechkarev.vault.features.info.InfoScreenNews.SetWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowLoginCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowNotesCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.info.InfoScreenNews.ShowWebsiteNameCopied
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnInit
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnLoginActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnLoginTextChanged
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnWebsiteNameActionClicked
import com.arsvechkarev.vault.features.info.InfoScreenUiEvent.OnWebsiteNameTextChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageServiceNameSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginExtraLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.backgroundColor
import viewdsl.circleRippleBackground
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.font
import viewdsl.getDrawableHeight
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.padding
import viewdsl.paddings
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class InfoScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    fun ConstraintLayout.Image(
      type: ImageType,
      id: Int,
      event: InfoScreenUiEvent,
      block: ImageView.() -> Unit
    ) {
      ImageView(WrapContent, WrapContent) {
        id(id)
        image(when (type) {
          OPEN_IN_NEW -> R.drawable.ic_open_in_new
          COPY -> R.drawable.ic_copy
        })
        padding(Dimens.IconPadding)
        circleRippleBackground(rippleColor = Colors.Ripple)
        onClick { store.tryDispatch(event) }
        apply(block)
      }
    }
    RootFrameLayout {
      id(InfoScreenRoot)
      backgroundColor(Colors.Background)
      ScrollableConstraintLayout(MatchParent, MatchParent) {
        apply(Styles.BaseRootBackground)
        paddings(
          top = MarginNormal + StatusBarHeight,
          start = MarginNormal,
          end = MarginNormal,
          bottom = MarginNormal,
        )
        clipToPadding = false
        ImageView(WrapContent, WrapContent, style = Styles.IconBack) {
          id(IconBack)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(IconDelete)
          image(R.drawable.ic_delete)
          imageTintList = ColorStateList.valueOf(Colors.Error)
          padding(Dimens.IconPadding)
          circleRippleBackground(Colors.ErrorRipple)
          onClick { store.tryDispatch(OnDeleteClicked) }
          constraints {
            topToTopOf(parent)
            endToEndOf(parent)
          }
        }
        ImageView(ImageServiceNameSize, ImageServiceNameSize) {
          id(ImageWebsite)
          scaleType = ImageView.ScaleType.FIT_XY
          margins(top = MarginExtraLarge)
          constraints {
            startToStartOf(parent)
            endToEndOf(parent)
            topToTopOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleWebsiteName)
          val gradientHeight = context.getDrawableHeight(R.drawable.bg_gradient) * 0.8
          margins(top = gradientHeight.toInt())
          text(R.string.text_website_name)
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(TextWebsiteName)
          setSingleLine()
          textSize(TextSizes.H1)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(ImageWebsite)
            startToStartOf(parent)
            endToEndOf(parent)
          }
        }
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.hint_website_name)) {
          id(EditTextWebsiteName)
          margins(end = MarginNormal)
          constraints {
            topToBottomOf(TitleWebsiteName)
            startToStartOf(TitleWebsiteName)
            endToStartOf(ButtonWebsiteNameAction)
          }
        }
        Image(COPY, ButtonWebsiteNameAction, OnWebsiteNameActionClicked) {
          constraints {
            topToTopOf(EditTextWebsiteName)
            bottomToBottomOf(EditTextWebsiteName)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleLogin)
          text(R.string.text_login)
          margins(top = MarginLarge)
          constraints {
            topToBottomOf(EditTextWebsiteName)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.hint_login)) {
          id(EditTextLogin)
          margins(end = MarginNormal)
          constraints {
            topToBottomOf(TitleLogin)
            startToStartOf(TitleLogin)
            endToStartOf(ButtonLoginAction)
          }
        }
        Image(COPY, ButtonLoginAction, OnLoginActionClicked) {
          constraints {
            topToTopOf(EditTextLogin)
            bottomToBottomOf(EditTextLogin)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitlePassword)
          margins(top = MarginLarge)
          text(R.string.text_password)
          constraints {
            topToBottomOf(EditTextLogin)
            startToStartOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(TextHiddenPassword)
          textSize(TextSizes.H1)
          text(R.string.text_password_stub)
          margins(top = MarginSmall)
          constraints {
            topToBottomOf(TitlePassword)
            startToStartOf(parent)
          }
        }
        Image(OPEN_IN_NEW, ButtonEditPassword, OnOpenPasswordScreenClicked) {
          margins(end = MarginMedium)
          constraints {
            topToTopOf(TextHiddenPassword)
            bottomToBottomOf(TextHiddenPassword)
            endToStartOf(ButtonCopyPassword)
          }
        }
        Image(COPY, ButtonCopyPassword, OnCopyPasswordClicked) {
          constraints {
            topToTopOf(TextHiddenPassword)
            bottomToBottomOf(TextHiddenPassword)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleNotes)
          text(R.string.text_notes)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(TextHiddenPassword)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.hint_notes)) {
          id(EditTextNotes)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H4)
          isSingleLine = false
          paddings(bottom = MarginExtraLarge)
          margins(end = MarginNormal)
          constraints {
            topToBottomOf(TitleNotes)
            startToStartOf(TitleNotes)
            endToStartOf(ButtonNotesAction)
          }
        }
        Image(COPY, ButtonNotesAction, OnNotesActionClicked) {
          paddings(top = MarginSmall)
          constraints {
            topToTopOf(EditTextNotes)
            endToEndOf(parent)
          }
        }
      }
      LoadingDialog()
      InfoDialog()
      child<Snackbar>(MatchParent, WrapContent) {
        classNameTag()
        layoutGravity(Gravity.BOTTOM)
        margin(MarginNormal)
      }
    }
  }
  
  private val store by viewModelStore {
    InfoScreenStore(coreComponent, arg(PasswordInfoItem::class))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  private val websiteNameTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnWebsiteNameTextChanged(it)) }
  
  private val loginTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnLoginTextChanged(it)) }
  
  private val notesTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnNotesTextChanged(it)) }
  
  private fun render(state: InfoScreenState) {
    imageView(ImageWebsite).setWebsiteIcon(state.websiteNameState.editedText)
    textView(TextWebsiteName).text(state.websiteNameState.editedText)
    renderTextState(EditTextWebsiteName, state.websiteNameState, ButtonWebsiteNameAction)
    renderTextState(EditTextLogin, state.loginState, ButtonLoginAction)
    renderTextState(EditTextNotes, state.notesState, ButtonNotesAction)
    if (!state.isEditingSomething) {
      requireContext().hideKeyboard()
    }
    if (state.showDeletePasswordDialog) {
      showInfoDialog(state)
    } else {
      infoDialog.hide()
    }
    if (state.showLoadingDialog) {
      loadingDialog.show()
    } else {
      loadingDialog.hide()
    }
  }
  
  private fun renderTextState(
    editTextId: Int,
    textState: TextState,
    actionImageId: Int
  ) {
    val icon = if (textState.isEditingNow) R.drawable.ic_checmark else R.drawable.ic_copy
    imageView(actionImageId).image(icon)
    if (!textState.isEditingNow) {
      editText(editTextId).clearFocus()
    }
  }
  
  
  private fun showInfoDialog(state: InfoScreenState) {
    infoDialog.showWithCancelAndProceedOption(
      titleRes = R.string.text_deleting_password,
      messageRes = getDeleteMessageText(state.websiteNameState.initialText),
      onCancel = { store.tryDispatch(OnDialogHidden) },
      onProceed = { store.tryDispatch(OnConfirmedDeletion) }
    )
  }
  
  private fun handleNews(news: InfoScreenNews) {
    val snackbar = viewAs<Snackbar>()
    when (news) {
      is SetWebsiteName -> {
        editText(EditTextWebsiteName).setTextSilently(news.websiteName, websiteNameTextWatcher)
      }
  
      is SetLogin -> {
        editText(EditTextLogin).setTextSilently(news.login, loginTextWatcher)
      }
  
      is SetNotes -> {
        editText(EditTextNotes).setTextSilently(news.notes, notesTextWatcher)
      }
  
      ShowWebsiteNameCopied -> {
        snackbar.show(R.string.text_website_name_copied)
      }
  
      ShowLoginCopied -> {
        snackbar.show(R.string.text_login_copied)
      }
  
      ShowPasswordCopied -> {
        snackbar.show(R.string.text_password_copied)
      }
  
      ShowNotesCopied -> {
        snackbar.show(R.string.text_notes_copied)
      }
    }
  }
  
  private fun EditText.setTextSilently(newText: String, textWatcher: BaseTextWatcher) {
    removeTextChangedListener(textWatcher)
    setText(newText)
    addTextChangedListener(textWatcher)
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private enum class ImageType {
    OPEN_IN_NEW, COPY
  }
  
  companion object {
  
    val InfoScreenRoot = View.generateViewId()
    val IconBack = View.generateViewId()
    val IconDelete = View.generateViewId()
    val ImageWebsite = View.generateViewId()
    val TextWebsiteName = View.generateViewId()
    val TitleWebsiteName = View.generateViewId()
    val EditTextWebsiteName = View.generateViewId()
    val ButtonWebsiteNameAction = View.generateViewId()
    val TitleLogin = View.generateViewId()
    val EditTextLogin = View.generateViewId()
    val ButtonLoginAction = View.generateViewId()
    val TitlePassword = View.generateViewId()
    val TextHiddenPassword = View.generateViewId()
    val ButtonEditPassword = View.generateViewId()
    val ButtonCopyPassword = View.generateViewId()
    val TitleNotes = View.generateViewId()
    val EditTextNotes = View.generateViewId()
    val ButtonNotesAction = View.generateViewId()
  }
}
