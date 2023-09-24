package com.arsvechkarev.vault.features.password_entry

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.stringArg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.Snackbar
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.common.setIconForTitle
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SetUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUsernameCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.ImageType.COPY
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.ImageType.OPEN_IN_NEW
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.SavePasswordEntryEventReceived
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageTitleSize
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
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
import viewdsl.setTextSilently
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class PasswordEntryScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context): View = context.withViewBuilder {
    fun ConstraintLayout.Image(
      type: ImageType,
      id: Int,
      event: PasswordEntryUiEvent,
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
      id(PasswordEntryScreenRoot)
      ScrollableConstraintLayout(MatchParent, MatchParent) {
        apply(Styles.BaseRootBackground)
        paddings(
          top = MarginNormal + StatusBarHeight,
          start = MarginNormal,
          end = MarginNormal,
          bottom = MarginNormal,
        )
        clipToPadding = false
        ImageView(WrapContent, WrapContent, style = Styles.ImageBack) {
          id(ImageBack)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageDelete)
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
        ImageView(ImageTitleSize, ImageTitleSize) {
          id(ImageTitle)
          scaleType = ImageView.ScaleType.FIT_XY
          margins(top = MarginExtraLarge)
          constraints {
            startToStartOf(parent)
            endToEndOf(parent)
            topToTopOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(Title)
          val gradientHeight = context.getDrawableHeight(R.drawable.bg_gradient) * 0.8
          margins(top = gradientHeight.toInt())
          text(R.string.text_title)
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(TextTitle)
          setSingleLine()
          textSize(TextSizes.H1)
          margins(top = MarginNormal)
          constraints {
            topToBottomOf(ImageTitle)
            startToStartOf(parent)
            endToEndOf(parent)
          }
        }
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.text_enter_title)) {
          id(EditTextTitle)
          margins(end = MarginNormal)
          addTextChangedListener(titleTextWatcher)
          constraints {
            topToBottomOf(Title)
            startToStartOf(Title)
            endToStartOf(ImageTitleAction)
          }
        }
        Image(COPY, ImageTitleAction, OnTitleActionClicked) {
          constraints {
            topToTopOf(EditTextTitle)
            bottomToBottomOf(EditTextTitle)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleUsername)
          text(R.string.text_username)
          margins(top = MarginLarge)
          constraints {
            topToBottomOf(EditTextTitle)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.text_enter_username)) {
          id(EditTextUsername)
          margins(end = MarginNormal)
          addTextChangedListener(usernameTextWatcher)
          constraints {
            topToBottomOf(TitleUsername)
            startToStartOf(TitleUsername)
            endToStartOf(ImageUsernameAction)
          }
        }
        Image(COPY, ImageUsernameAction, OnUsernameActionClicked) {
          constraints {
            topToTopOf(EditTextUsername)
            bottomToBottomOf(EditTextUsername)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitlePassword)
          margins(top = MarginLarge)
          text(R.string.text_password)
          constraints {
            topToBottomOf(EditTextUsername)
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
        Image(OPEN_IN_NEW, ImageEditPassword, OnOpenPasswordScreenClicked) {
          margins(end = MarginMedium)
          constraints {
            topToTopOf(TextHiddenPassword)
            bottomToBottomOf(TextHiddenPassword)
            endToStartOf(ImageCopyPassword)
          }
        }
        Image(COPY, ImageCopyPassword, OnCopyPasswordClicked) {
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
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.text_add_notes)) {
          id(EditTextNotes)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H4)
          isSingleLine = false
          paddings(bottom = MarginExtraLarge)
          margins(end = MarginNormal)
          addTextChangedListener(notesTextWatcher)
          constraints {
            topToBottomOf(TitleNotes)
            startToStartOf(TitleNotes)
            endToStartOf(ImageNotesAction)
          }
        }
        Image(COPY, ImageNotesAction, OnNotesActionClicked) {
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
    PasswordEntryStore(coreComponent, stringArg(PasswordItem::class.qualifiedName!!))
  }
  
  private val titleTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnTitleTextChanged(it)) }
  
  private val usernameTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnUsernameTextChanged(it)) }
  
  private val notesTextWatcher =
      BaseTextWatcher { store.tryDispatch(OnNotesTextChanged(it)) }
  
  override fun onInit() {
    CreatingPasswordCommunication.communicator.output
        .onEach { event -> store.tryDispatch(SavePasswordEntryEventReceived(event.password)) }
        .launchIn(lifecycleScope)
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  private fun render(state: PasswordEntryState) {
    imageView(ImageTitle).setIconForTitle(state.titleState.editedText)
    textView(TextTitle).text(state.titleState.editedText)
    renderTextState(EditTextTitle, state.titleState, ImageTitleAction)
    renderTextState(EditTextUsername, state.usernameState, ImageUsernameAction)
    renderTextState(EditTextNotes, state.notesState, ImageNotesAction)
    if (!state.isEditingSomething) {
      requireContext().hideKeyboard()
    }
    if (state.showTitleIsEmptyError) {
      textView(Title).text(R.string.text_title_is_empty)
      textView(Title).textColor(Colors.Error)
    } else {
      textView(Title).text(R.string.text_title)
      textView(Title).textColor(Colors.Accent)
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
    val image = if (textState.isEditingNow) R.drawable.ic_checmark else R.drawable.ic_copy
    imageView(actionImageId).image(image)
    if (!textState.isEditingNow) {
      editText(editTextId).clearFocus()
    }
  }
  
  private fun showInfoDialog(state: PasswordEntryState) {
    infoDialog.showWithCancelAndProceedOption(
      titleRes = R.string.text_delete_password,
      messageRes = getDeleteMessageText(state.titleState.initialText),
      onCancel = { store.tryDispatch(OnDialogHidden) },
      onProceed = { store.tryDispatch(OnConfirmedDeletion) }
    )
  }
  
  private fun handleNews(news: PasswordEntryNews) {
    val snackbar = viewAs<Snackbar>()
    when (news) {
      is SetTitle ->
        editText(EditTextTitle)
            .setTextSilently(news.title, titleTextWatcher)
      is SetUsername -> editText(EditTextUsername).setTextSilently(news.username,
        usernameTextWatcher)
      is SetNotes -> editText(EditTextNotes).setTextSilently(news.notes, notesTextWatcher)
      ShowTitleCopied -> snackbar.show(R.string.text_title_copied)
      ShowUsernameCopied -> snackbar.show(R.string.text_username_copied)
      ShowPasswordCopied -> snackbar.show(R.string.text_password_copied)
      ShowNotesCopied -> snackbar.show(R.string.text_notes_copied)
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private enum class ImageType {
    OPEN_IN_NEW, COPY
  }
  
  companion object {
    
    val PasswordEntryScreenRoot = View.generateViewId()
    val ImageBack = View.generateViewId()
    val ImageDelete = View.generateViewId()
    val ImageTitle = View.generateViewId()
    val TextTitle = View.generateViewId()
    val Title = View.generateViewId()
    val EditTextTitle = View.generateViewId()
    val ImageTitleAction = View.generateViewId()
    val TitleUsername = View.generateViewId()
    val EditTextUsername = View.generateViewId()
    val ImageUsernameAction = View.generateViewId()
    val TitlePassword = View.generateViewId()
    val TextHiddenPassword = View.generateViewId()
    val ImageEditPassword = View.generateViewId()
    val ImageCopyPassword = View.generateViewId()
    val TitleNotes = View.generateViewId()
    val EditTextNotes = View.generateViewId()
    val ImageNotesAction = View.generateViewId()
  }
}
