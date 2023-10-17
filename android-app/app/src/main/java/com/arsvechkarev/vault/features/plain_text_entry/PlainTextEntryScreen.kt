package com.arsvechkarev.vault.features.plain_text_entry

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.stringNullableArg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
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
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.SetText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.SetTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowPlainTextEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowTextCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryNews.ShowTitleCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnFavoriteClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnInit
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryUiEvent.OnTitleChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens.CircleButtonSize
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.IconPadding
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginMedium
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginSmall
import com.arsvechkarev.vault.viewbuilding.Styles
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.Size.IntSize
import viewdsl.circleRippleBackground
import viewdsl.constraints
import viewdsl.gone
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.imageTint
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
import viewdsl.withViewBuilder

class PlainTextEntryScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      id(PlainTextScreenRoot)
      ScrollableConstraintLayout {
        paddings(bottom = MarginNormal + CircleButtonSize)
        ImageView(WrapContent, WrapContent, style = Styles.ImageBack) {
          id(ImageBack)
          margins(start = MarginSmall, top = StatusBarHeight + MarginMedium)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(MainTitle)
          text(R.string.text_new_plain_text)
          textSize(TextSizes.H1)
          margins(start = MarginNormal)
          constraints {
            startToEndOf(ImageBack)
            topToTopOf(ImageBack)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageDelete)
          padding(IconPadding)
          margins(start = MarginNormal, end = MarginNormal)
          image(R.drawable.ic_delete)
          imageTint(Colors.Error)
          circleRippleBackground(Colors.ErrorRipple)
          onClick { store.tryDispatch(OnDeleteClicked) }
          constraints {
            topToTopOf(MainTitle)
            endToEndOf(parent)
            bottomToBottomOf(MainTitle)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageFavorite)
          image(R.drawable.ic_star_outline)
          imageTint(Colors.Favorite)
          padding(IconPadding)
          margins(end = MarginSmall)
          circleRippleBackground(Colors.FavoriteRipple)
          onClick { store.tryDispatch(OnFavoriteClicked) }
          constraints {
            topToTopOf(ImageDelete)
            endToStartOf(ImageDelete)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(Title)
          margins(start = MarginNormal, top = GradientDrawableHeight)
          text(R.string.text_title)
          constraints {
            topToTopOf(parent)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.text_enter_title)) {
          id(EditTextTitle)
          margins(start = MarginNormal, end = MarginNormal)
          onSubmit { editText(EditTextText).requestFocus() }
          addTextChangedListener(titleTextWatcher)
          constraints {
            topToBottomOf(Title)
            startToStartOf(parent)
            endToStartOf(ImageTitleAction)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageTitleAction)
          image(R.drawable.ic_copy)
          padding(IconPadding)
          margins(end = MarginNormal)
          circleRippleBackground(rippleColor = Colors.Ripple)
          onClick { store.tryDispatch(OnTitleActionClicked) }
          constraints {
            topToTopOf(EditTextTitle)
            bottomToBottomOf(EditTextTitle)
            endToEndOf(parent)
          }
        }
        TextView(WrapContent, WrapContent, style = AccentTextView) {
          id(TitleText)
          margins(start = MarginNormal, top = MarginLarge)
          text(R.string.text_text)
          constraints {
            topToBottomOf(EditTextTitle)
            startToStartOf(parent)
          }
        }
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.hint_text)) {
          id(EditTextText)
          isSingleLine = false
          margins(start = MarginNormal, end = MarginNormal)
          onSubmit { store.tryDispatch(OnSaveClicked) }
          addTextChangedListener(textTextWatcher)
          constraints {
            topToBottomOf(TitleText)
            startToStartOf(parent)
            endToEndOf(ImageTextAction)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageTextAction)
          image(R.drawable.ic_copy)
          padding(IconPadding)
          margins(end = MarginNormal)
          circleRippleBackground(rippleColor = Colors.Ripple)
          onClick { store.tryDispatch(OnTextActionClicked) }
          constraints {
            topToTopOf(EditTextText)
            endToEndOf(parent)
          }
        }
      }
      ImageView(
        width = IntSize(CircleButtonSize),
        height = IntSize(CircleButtonSize),
        style = Styles.CircleCheckmarkButton
      ) {
        id(ButtonSave)
        gone()
        margin(MarginNormal)
        onClick { store.tryDispatch(OnSaveClicked) }
        layoutGravity(Gravity.BOTTOM or Gravity.END)
      }
      InfoDialog()
      LoadingDialog()
      Snackbar {
        layoutGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        margin(MarginNormal)
      }
    }
  }
  
  private val titleTextWatcher = BaseTextWatcher { store.tryDispatch(OnTitleChanged(it)) }
  private val textTextWatcher = BaseTextWatcher { store.tryDispatch(OnTextChanged(it)) }
  
  private val store by viewModelStore {
    PlainTextEntryStore(coreComponent, stringNullableArg(PlainTextItem::class.qualifiedName!!))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  override fun onAppearedOnScreen() {
    if (stringNullableArg(PlainTextItem::class.qualifiedName!!) == null) {
      requireView().postDelayed({
        viewAsNullable<EditText>(EditTextTitle)?.apply {
          requestFocus()
          requireContext().showKeyboard(this)
        }
      }, Durations.DelayOpenKeyboard)
    }
  }
  
  private fun render(state: PlainTextEntryState) {
    view(ButtonSave).isVisible = state is NewEntry
    view(ImageTitleAction).isVisible = state is ExistingEntry
    view(ImageTextAction).isVisible = state is ExistingEntry
    view(ImageFavorite).isVisible = state is ExistingEntry
    view(ImageDelete).isVisible = state is ExistingEntry
    val resId = if (state is NewEntry) R.string.text_new_plain_text else R.string.text_plain_text
    textView(MainTitle).text(resId)
    when (state) {
      is NewEntry -> renderNewEntry(state)
      is ExistingEntry -> renderExistingEntry(state)
    }
  }
  
  private fun renderNewEntry(state: NewEntry) {
    imageView(ImageFavorite).gone()
    if (state.showTitleIsEmptyError) {
      showTitleViewError()
    } else {
      showTitleViewDefault()
    }
  }
  
  private fun renderExistingEntry(state: ExistingEntry) {
    if (state.plainTextEntry?.isFavorite == true) {
      imageView(ImageFavorite).image(R.drawable.ic_star_filled)
    } else {
      imageView(ImageFavorite).image(R.drawable.ic_star_outline)
    }
    renderTextState(EditTextTitle, state.titleState, ImageTitleAction)
    renderTextState(EditTextText, state.textState, ImageTextAction)
    if (!state.isEditingSomething) {
      requireContext().hideKeyboard()
    }
    if (state.showTitleIsEmptyError) {
      showTitleViewError()
    } else {
      showTitleViewDefault()
    }
    if (state.showConfirmDeleteDialog) {
      infoDialog.showWithCancelAndProceedOption(
        titleRes = R.string.text_delete_plain_text,
        messageRes = getDeleteMessageText(state.titleState.initialText),
        onCancel = { store.tryDispatch(OnDialogHidden) },
        onProceed = { store.tryDispatch(OnConfirmedDeleting) }
      )
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
  
  private fun handleNews(news: PlainTextEntryNews) {
    when (news) {
      is SetTitle -> editText(EditTextTitle).setTextSilently(news.title, titleTextWatcher)
      is SetText -> editText(EditTextText).setTextSilently(news.text, textTextWatcher)
      ShowTitleCopied -> snackbar.show(CHECKMARK, R.string.text_title_copied)
      ShowTextCopied -> snackbar.show(CHECKMARK, R.string.text_text_copied)
      ShowPlainTextEntryCreated -> {
        snackbar.show(CHECKMARK, R.string.text_plain_text_created)
        requireContext().hideKeyboard()
        editText(EditTextTitle).clearFocus()
        editText(EditTextText).clearFocus()
      }
    }
  }
  
  override fun onDisappearedFromScreen() {
    editText(EditTextTitle).clearFocus()
    editText(EditTextText).clearFocus()
    requireContext().hideKeyboard()
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private fun showTitleViewDefault() {
    textView(Title).apply {
      textColor(Colors.Accent)
      text(R.string.text_title)
    }
  }
  
  private fun showTitleViewError() {
    textView(Title).apply {
      textColor(Colors.Error)
      text(R.string.text_title_is_empty)
    }
  }
  
  companion object {
    
    val PlainTextScreenRoot = View.generateViewId()
    val ImageBack = View.generateViewId()
    val MainTitle = View.generateViewId()
    val ImageDelete = View.generateViewId()
    val ImageFavorite = View.generateViewId()
    val Title = View.generateViewId()
    val EditTextTitle = View.generateViewId()
    val ImageTitleAction = View.generateViewId()
    val TitleText = View.generateViewId()
    val EditTextText = View.generateViewId()
    val ImageTextAction = View.generateViewId()
    val ButtonSave = View.generateViewId()
  }
}
