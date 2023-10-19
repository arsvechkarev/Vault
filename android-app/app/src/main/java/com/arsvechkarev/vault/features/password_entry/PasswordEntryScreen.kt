package com.arsvechkarev.vault.features.password_entry

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.stringNullableArg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.Snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Companion.Snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Companion.snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Type.CHECKMARK
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.domain.setIconForTitle
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.OpenUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ReloadTitleIcon
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestNotesFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestUrlFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.RequestUsernameFocus
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowNotesCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowPasswordCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUrlCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.ShowUsernameCopied
import com.arsvechkarev.vault.features.password_entry.PasswordEntryNews.SwitchToExistingEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.ImageType.COPY
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.ImageType.OPEN_IN_NEW
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.ImageType.WEB
import com.arsvechkarev.vault.features.password_entry.PasswordEntryState.ExistingEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryState.NewEntry
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnConfirmedDeletion
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnCopyPasswordClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextNotesSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextTitleSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextUrlSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnEditTextUsernameSubmitClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnFavoriteClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnHideDeleteDialog
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnImagesLoadingFailed
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnNotesTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenPasswordScreenClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnOpenUrlClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnTitleTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUrlActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUrlTextChanged
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameActionClicked
import com.arsvechkarev.vault.features.password_entry.PasswordEntryUiEvent.OnUsernameTextChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.CircleButtonSize
import com.arsvechkarev.vault.viewbuilding.Dimens.ImageTitleSize
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginBig
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.CircleCheckmarkButton
import com.arsvechkarev.vault.viewbuilding.Styles.SecondaryTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import domain.Password
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.Size.IntSize
import viewdsl.circleRippleBackground
import viewdsl.constraints
import viewdsl.font
import viewdsl.gone
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.imageTint
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onSubmit
import viewdsl.padding
import viewdsl.paddings
import viewdsl.setTextSilently
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.visible
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
          WEB -> R.drawable.ic_web
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
        id(ScrollableConstraintLayoutId)
        paddings(
          top = MarginMedium + StatusBarHeight,
          start = MarginNormal,
          end = MarginNormal,
          bottom = MarginNormal + CircleButtonSize,
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
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(TitleNewPassword)
          text(R.string.text_new_password)
          margins(start = MarginNormal)
          textSize(TextSizes.H1)
          invisible()
          constraints {
            startToEndOf(ImageBack)
            topToTopOf(ImageBack)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageDelete)
          image(R.drawable.ic_delete)
          imageTint(Colors.Error)
          padding(Dimens.IconPadding)
          circleRippleBackground(Colors.ErrorRipple)
          onClick { store.tryDispatch(OnDeleteClicked) }
          constraints {
            topToTopOf(parent)
            endToEndOf(parent)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageFavorite)
          image(R.drawable.ic_star_outline)
          imageTint(Colors.Favorite)
          padding(Dimens.IconPadding)
          margins(end = MarginSmall)
          circleRippleBackground(Colors.FavoriteRipple)
          onClick { store.tryDispatch(OnFavoriteClicked) }
          constraints {
            topToTopOf(parent)
            endToStartOf(ImageDelete)
          }
        }
        ImageView(ImageTitleSize, ImageTitleSize) {
          id(ImageTitle)
          scaleType = ImageView.ScaleType.FIT_XY
          margins(top = MarginBig)
          constraints {
            startToStartOf(parent)
            endToEndOf(parent)
            topToBottomOf(ImageBack)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(Title)
          margins(top = MarginBig)
          text(R.string.text_title)
          constraints {
            topToBottomOf(ImageTitle)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.text_enter_title)) {
          id(EditTextTitle)
          margins(end = MarginNormal)
          addTextChangedListener(titleTextWatcher)
          onSubmit { store.tryDispatch(OnEditTextTitleSubmitClicked) }
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
          onSubmit { store.tryDispatch(OnEditTextUsernameSubmitClicked) }
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
          id(TextPassword)
          margins(top = MarginSmall)
          constraints {
            topToBottomOf(TitlePassword)
            startToStartOf(parent)
          }
        }
        View(ZERO, ZERO) {
          id(ItemGeneratePassword)
          onClick { store.tryDispatch(OnOpenPasswordScreenClicked) }
        }
        Image(OPEN_IN_NEW, ImageEditPassword, OnOpenPasswordScreenClicked) {
          margins(end = MarginMedium)
        }
        Image(COPY, ImageCopyPassword, OnCopyPasswordClicked) {
          constraints {
            topToTopOf(TextPassword)
            bottomToBottomOf(TextPassword)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleUrl)
          text(R.string.text_url)
          margins(top = MarginLarge)
          constraints {
            topToBottomOf(TextPassword)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.text_enter_url)) {
          id(EditTextUrl)
          margins(end = MarginNormal)
          addTextChangedListener(urlTextWatcher)
          onSubmit { store.tryDispatch(OnEditTextUrlSubmitClicked) }
          constraints {
            topToBottomOf(TitleUrl)
            startToStartOf(TitleUrl)
            endToStartOf(ImageOpenUrl)
          }
        }
        Image(WEB, ImageOpenUrl, OnOpenUrlClicked) {
          margins(end = TextPassword)
          constraints {
            topToTopOf(EditTextUrl)
            bottomToBottomOf(EditTextUrl)
            endToStartOf(ImageUrlAction)
          }
        }
        Image(COPY, ImageUrlAction, OnUrlActionClicked) {
          constraints {
            topToTopOf(EditTextUrl)
            bottomToBottomOf(EditTextUrl)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleNotes)
          text(R.string.text_notes)
          margins(top = MarginLarge)
          constraints {
            topToBottomOf(EditTextUrl)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, BaseEditText(hint = R.string.text_add_notes)) {
          id(EditTextNotes)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H4)
          isSingleLine = false
          margins(end = MarginNormal)
          addTextChangedListener(notesTextWatcher)
          onSubmit { store.tryDispatch(OnEditTextNotesSubmitClicked) }
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
      ImageView(
        width = IntSize(CircleButtonSize),
        height = IntSize(CircleButtonSize),
        style = CircleCheckmarkButton
      ) {
        id(ButtonSave)
        gone()
        margin(MarginNormal)
        onClick { store.tryDispatch(OnSaveClicked) }
        layoutGravity(Gravity.BOTTOM or Gravity.END)
      }
      LoadingDialog()
      InfoDialog()
      Snackbar {
        layoutGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        margin(MarginNormal)
      }
    }
  }
  
  private val store by viewModelStore {
    PasswordEntryStore(coreComponent, stringNullableArg(PasswordItem::class.java.name))
  }
  
  private val titleTextWatcher = BaseTextWatcher { store.tryDispatch(OnTitleTextChanged(it)) }
  
  private val usernameTextWatcher = BaseTextWatcher { store.tryDispatch(OnUsernameTextChanged(it)) }
  
  private val urlTextWatcher = BaseTextWatcher { store.tryDispatch(OnUrlTextChanged(it)) }
  
  private val notesTextWatcher = BaseTextWatcher { store.tryDispatch(OnNotesTextChanged(it)) }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  override fun onViewCreated() {
    val password = stringNullableArg(PasswordItem::class.java.name)
    if (password != null) {
      initExistingEntry()
    } else {
      initNewEntry()
    }
  }
  
  private fun initNewEntry() {
    view(TitleNewPassword).visible()
    view(ButtonSave).visible()
    view(ImageDelete).gone()
    view(ImageFavorite).gone()
    view(ImageTitleAction).gone()
    view(ImageUsernameAction).gone()
    view(ImageOpenUrl).gone()
    view(ImageUrlAction).gone()
    view(ImageCopyPassword).gone()
    view(ImageNotesAction).gone()
    view(ImageEditPassword).constraints {
      topToTopOf(TextPassword)
      bottomToBottomOf(TextPassword)
      endToEndOf(parent)
    }
    view(ItemGeneratePassword).constraints {
      startToStartOf(parent)
      topToTopOf(TitlePassword)
      endToEndOf(parent)
      bottomToBottomOf(TextPassword)
    }
    requireView().postDelayed({
      viewAsNullable<EditText>(EditTextTitle)?.apply {
        requestFocus()
        requireContext().showKeyboard(this)
      }
    }, Durations.DelayOpenKeyboard)
  }
  
  private fun initExistingEntry() {
    view(TitleNewPassword).gone()
    view(ButtonSave).gone()
    view(ImageDelete).visible()
    view(ImageFavorite).visible()
    view(ImageTitleAction).visible()
    view(ImageUsernameAction).visible()
    view(ImageUrlAction).visible()
    view(ImageCopyPassword).visible()
    view(ImageNotesAction).visible()
    view(ImageEditPassword).constraints {
      topToTopOf(TextPassword)
      bottomToBottomOf(TextPassword)
      endToStartOf(ImageCopyPassword)
    }
    view(ItemGeneratePassword).constraints {
      startToStartOf(parent)
      topToTopOf(TitlePassword)
      endToStartOf(ImageCopyPassword)
      bottomToBottomOf(TextPassword)
    }
  }
  
  private fun render(state: PasswordEntryState) {
    when (state) {
      is NewEntry -> renderNewEntry(state)
      is ExistingEntry -> renderExistingEntry(state)
    }
  }
  
  private fun renderNewEntry(state: NewEntry) {
    imageView(ImageTitle).setIconForTitle(state.title, onImageLoadingFailed = {
      store.tryDispatch(OnImagesLoadingFailed)
    })
    if (state.showTitleEmptyError) {
      textView(Title).text(R.string.text_title_is_empty)
      textView(Title).textColor(Colors.Error)
    } else {
      textView(Title).text(R.string.text_title)
      textView(Title).textColor(Colors.Accent)
    }
    editText(EditTextTitle).setTextSilently(state.title, titleTextWatcher)
    editText(EditTextUsername).setTextSilently(state.username, usernameTextWatcher)
    editText(EditTextUrl).setTextSilently(state.url, urlTextWatcher)
    editText(EditTextNotes).setTextSilently(state.notes, notesTextWatcher)
    renderPasswordText(state.password)
  }
  
  private fun renderExistingEntry(state: ExistingEntry) {
    when (state.passwordEntry?.isFavorite) {
      true -> imageView(ImageFavorite).image(R.drawable.ic_star_filled)
      false -> imageView(ImageFavorite).image(R.drawable.ic_star_outline)
      else -> Unit
    }
    imageView(ImageTitle).setIconForTitle(state.titleState.editedText, onImageLoadingFailed = {
      store.tryDispatch(OnImagesLoadingFailed)
    })
    editText(EditTextTitle).setTextSilently(state.titleState.editedText, titleTextWatcher)
    editText(EditTextUsername).setTextSilently(state.usernameState.editedText, usernameTextWatcher)
    editText(EditTextUrl).setTextSilently(state.urlState.editedText, urlTextWatcher)
    editText(EditTextNotes).setTextSilently(state.notesState.editedText, notesTextWatcher)
    renderActionIcons(EditTextTitle, state.titleState, ImageTitleAction)
    renderActionIcons(EditTextUsername, state.usernameState, ImageUsernameAction)
    renderActionIcons(EditTextUrl, state.urlState, ImageUrlAction)
    renderActionIcons(EditTextNotes, state.notesState, ImageNotesAction)
    renderPasswordText(state.passwordEntry?.password)
    val urlNotEmpty = state.urlState.editedText.isNotBlank()
    view(ImageOpenUrl).isVisible = urlNotEmpty
    editText(EditTextUrl).paint.isUnderlineText = urlNotEmpty
    if (!state.isEditingSomething) {
      requireContext().hideKeyboard()
    }
    if (state.showTitleEmptyError) {
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
  
  private fun renderPasswordText(password: Password?) {
    if (password == null) {
      return
    } else if (password.isNotEmpty) {
      textView(TextPassword).apply {
        apply(BoldTextView)
        text(R.string.text_password_stub)
      }
    } else {
      textView(TextPassword).apply {
        apply(SecondaryTextView)
        textSize(TextSizes.H4)
        text(R.string.text_generate_password)
      }
    }
  }
  
  private fun renderActionIcons(
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
  
  private fun showInfoDialog(state: ExistingEntry) {
    infoDialog.showWithCancelAndProceedOption(
      titleRes = R.string.text_delete_password,
      messageRes = getDeleteMessageText(state.titleState.initialText),
      onCancel = { store.tryDispatch(OnHideDeleteDialog) },
      onProceed = { store.tryDispatch(OnConfirmedDeletion) }
    )
  }
  
  private fun handleNews(news: PasswordEntryNews) {
    val snackbar = viewAs<Snackbar>()
    when (news) {
      ShowTitleCopied -> snackbar.show(CHECKMARK, R.string.text_title_copied)
      ShowUsernameCopied -> snackbar.show(CHECKMARK, R.string.text_username_copied)
      ShowPasswordCopied -> snackbar.show(CHECKMARK, R.string.text_password_copied)
      ShowUrlCopied -> snackbar.show(CHECKMARK, R.string.text_url_copied)
      ShowNotesCopied -> snackbar.show(CHECKMARK, R.string.text_notes_copied)
      is ReloadTitleIcon -> {
        imageView(ImageTitle).setIconForTitle(news.title, onImageLoadingFailed = {
          store.tryDispatch(OnImagesLoadingFailed)
        })
      }
      RequestUsernameFocus -> {
        editText(EditTextUsername).requestFocus()
      }
      RequestUrlFocus -> {
        editText(EditTextUrl).requestFocus()
      }
      RequestNotesFocus -> {
        editText(EditTextNotes).requestFocus()
      }
      SwitchToExistingEntry -> {
        initExistingEntry()
      }
      is OpenUrl -> {
        openUrlIfPossible(news.url)
      }
    }
  }
  
  private fun openUrlIfPossible(url: String) {
    val resultUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
      "https://$url"
    } else {
      url
    }
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(resultUrl))
    if (browserIntent.resolveActivity(requireContext().packageManager) != null) {
      startActivity(browserIntent)
    } else {
      snackbar.show(CHECKMARK, R.string.text_browser_not_found)
    }
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private enum class ImageType {
    OPEN_IN_NEW, COPY, WEB
  }
  
  companion object {
    
    val PasswordEntryScreenRoot = View.generateViewId()
    val ScrollableConstraintLayoutId = View.generateViewId()
    val TitleNewPassword = View.generateViewId()
    val ImageBack = View.generateViewId()
    val ImageDelete = View.generateViewId()
    val ImageFavorite = View.generateViewId()
    val ImageTitle = View.generateViewId()
    val Title = View.generateViewId()
    val EditTextTitle = View.generateViewId()
    val ImageTitleAction = View.generateViewId()
    val TitleUsername = View.generateViewId()
    val EditTextUsername = View.generateViewId()
    val ImageUsernameAction = View.generateViewId()
    val TitlePassword = View.generateViewId()
    val TextPassword = View.generateViewId()
    val ItemGeneratePassword = View.generateViewId()
    val ImageEditPassword = View.generateViewId()
    val ImageCopyPassword = View.generateViewId()
    val TitleUrl = View.generateViewId()
    val EditTextUrl = View.generateViewId()
    val ImageOpenUrl = View.generateViewId()
    val ImageUrlAction = View.generateViewId()
    val TitleNotes = View.generateViewId()
    val EditTextNotes = View.generateViewId()
    val ImageNotesAction = View.generateViewId()
    val ButtonSave = View.generateViewId()
  }
}
