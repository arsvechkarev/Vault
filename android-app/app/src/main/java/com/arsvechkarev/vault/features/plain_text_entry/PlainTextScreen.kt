package com.arsvechkarev.vault.features.plain_text_entry

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.annotation.StringRes
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.extensions.getDeleteMessageText
import com.arsvechkarev.vault.core.extensions.nullableArg
import com.arsvechkarev.vault.core.mvi.ext.subscribe
import com.arsvechkarev.vault.core.mvi.ext.viewModelStore
import com.arsvechkarev.vault.core.views.Snackbar.Companion.Snackbar
import com.arsvechkarev.vault.core.views.Snackbar.Companion.snackbar
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.common.TextState
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder.coreComponent
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.infoDialog
import com.arsvechkarev.vault.features.common.dialogs.LoadingDialog
import com.arsvechkarev.vault.features.common.dialogs.loadingDialog
import com.arsvechkarev.vault.features.common.model.PlainTextItem
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.SetText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.SetTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowEntryCreated
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowTextCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextNews.ShowTitleCopied
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.ExistingEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextState.NewEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnBackPressed
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnConfirmedDeleting
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnConfirmedSaving
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnDeleteClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnDialogHidden
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnInit
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnSaveClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTextActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTextChanged
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTitleActionClicked
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextUiEvent.OnTitleChanged
import com.arsvechkarev.vault.viewbuilding.Colors
import com.arsvechkarev.vault.viewbuilding.Dimens
import com.arsvechkarev.vault.viewbuilding.Dimens.GradientDrawableHeight
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginLarge
import com.arsvechkarev.vault.viewbuilding.Dimens.MarginNormal
import com.arsvechkarev.vault.viewbuilding.Styles.AccentTextView
import com.arsvechkarev.vault.viewbuilding.Styles.BaseEditText
import com.arsvechkarev.vault.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.vault.viewbuilding.Styles.Button
import com.arsvechkarev.vault.viewbuilding.Styles.IconBack
import com.arsvechkarev.vault.viewbuilding.TextSizes
import navigation.BaseFragmentScreen
import viewdsl.BaseTextWatcher
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.Companion.ZERO
import viewdsl.circleRippleBackground
import viewdsl.constraints
import viewdsl.hideKeyboard
import viewdsl.id
import viewdsl.image
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.onClick
import viewdsl.onSubmit
import viewdsl.padding
import viewdsl.setSoftInputMode
import viewdsl.setTextSilently
import viewdsl.showKeyboard
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.withViewBuilder

class PlainTextScreen : BaseFragmentScreen() {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootFrameLayout {
      id(PlainTextScreenRoot)
      ScrollableConstraintLayout {
        ImageView(WrapContent, WrapContent, style = IconBack) {
          id(ImageBack)
          margins(start = MarginNormal, end = MarginNormal)
          onClick { store.tryDispatch(OnBackPressed) }
          constraints {
            topToTopOf(MainTitle)
            startToStartOf(parent)
            bottomToBottomOf(MainTitle)
          }
        }
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          id(MainTitle)
          text(R.string.text_new_plain_text)
          textSize(TextSizes.H1)
          margins(top = StatusBarHeight + MarginNormal)
          constraints {
            topToTopOf(parent)
            startToEndOf(ImageBack)
            endToStartOf(ImageDelete)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageDelete)
          padding(Dimens.IconPadding)
          margins(start = MarginNormal, end = MarginNormal)
          image(R.drawable.ic_delete)
          imageTintList = ColorStateList.valueOf(Colors.Error)
          circleRippleBackground(Colors.ErrorRipple)
          onClick { store.tryDispatch(OnDeleteClicked) }
          constraints {
            topToTopOf(MainTitle)
            endToEndOf(parent)
            bottomToBottomOf(MainTitle)
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
        EditText(ZERO, WrapContent, style = BaseEditText(hint = R.string.hint_title)) {
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
          padding(Dimens.IconPadding)
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
          id(Text)
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
            topToBottomOf(Text)
            startToStartOf(parent)
            endToEndOf(ImageTextAction)
          }
        }
        ImageView(WrapContent, WrapContent) {
          id(ImageTextAction)
          image(R.drawable.ic_copy)
          padding(Dimens.IconPadding)
          margins(end = MarginNormal)
          circleRippleBackground(rippleColor = Colors.Ripple)
          onClick { store.tryDispatch(OnTextActionClicked) }
          constraints {
            topToTopOf(EditTextText)
            endToEndOf(parent)
          }
        }
      }
      TextView(MatchParent, WrapContent, style = Button()) {
        id(ButtonSave)
        margins(start = MarginNormal, end = MarginNormal, bottom = MarginNormal)
        layoutGravity(Gravity.BOTTOM)
        text(R.string.text_save)
        onClick { store.tryDispatch(OnSaveClicked) }
      }
      InfoDialog()
      LoadingDialog()
      Snackbar {
        layoutGravity(Gravity.BOTTOM)
        margin(MarginNormal)
      }
    }
  }
  
  private val titleTextWatcher = BaseTextWatcher { store.tryDispatch(OnTitleChanged(it)) }
  private val textTextWatcher = BaseTextWatcher { store.tryDispatch(OnTextChanged(it)) }
  
  private val store by viewModelStore {
    PlainTextStore(coreComponent, nullableArg(PlainTextItem::class))
  }
  
  override fun onInit() {
    store.subscribe(this, ::render, ::handleNews)
    store.tryDispatch(OnInit)
  }
  
  override fun onAppearedOnScreen() {
    if (nullableArg(PlainTextItem::class) == null) {
      requireView().postDelayed({
        editText(EditTextTitle).apply {
          requestFocus()
          requireContext().showKeyboard(this)
        }
      }, Durations.DelayOpenKeyboard)
    }
  }
  
  private fun render(state: PlainTextState) {
    view(ButtonSave).isVisible = state is NewEntry
    view(ImageTitleAction).isVisible = state is ExistingEntry
    view(ImageTextAction).isVisible = state is ExistingEntry
    view(ImageDelete).isVisible = state is ExistingEntry
    val resId = if (state is NewEntry) R.string.text_new_plain_text else R.string.text_plain_text
    textView(MainTitle).text(resId)
    when (state) {
      is ExistingEntry -> renderExistingEntry(state)
      is NewEntry -> renderNewEntry(state)
    }
  }
  
  private fun renderNewEntry(state: NewEntry) {
    if (state.showTitleIsEmpty) {
      showAccentTextViewError(Title, R.string.text_title_cant_be_empty)
    } else {
      showAccentTextViewDefault(Title, R.string.text_title)
    }
    if (state.showConfirmSaveDialog) {
      infoDialog.showWithOkOption(
        R.string.text_confirmation,
        R.string.text_do_you_want_to_save_data,
        R.string.text_yes,
        onCancel = { store.tryDispatch(OnDialogHidden) },
        onOkClicked = { store.tryDispatch(OnConfirmedSaving) }
      )
    } else {
      infoDialog.hide()
    }
  }
  
  private fun renderExistingEntry(state: ExistingEntry) {
    renderTextState(EditTextTitle, state.titleState, ImageTitleAction)
    renderTextState(EditTextText, state.textState, ImageTextAction)
    if (!state.isEditingSomething) {
      requireContext().hideKeyboard()
    }
    if (state.showConfirmDeleteDialog) {
      infoDialog.showWithCancelAndProceedOption(
        titleRes = R.string.text_deleting_entry,
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
  
  private fun handleNews(news: PlainTextNews) {
    when (news) {
      is SetTitle -> editText(EditTextTitle).setTextSilently(news.title, titleTextWatcher)
      is SetText -> editText(EditTextText).setTextSilently(news.text, textTextWatcher)
      ShowTitleCopied -> snackbar.show(R.string.text_title_copied)
      ShowTextCopied -> snackbar.show(R.string.text_text_copied)
      ShowEntryCreated -> {
        snackbar.show(R.string.text_plain_text_created)
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
    requireContext().setSoftInputMode(SOFT_INPUT_ADJUST_RESIZE)
  }
  
  override fun handleBackPress(): Boolean {
    store.tryDispatch(OnBackPressed)
    return true
  }
  
  private fun showAccentTextViewDefault(textViewId: Int, @StringRes defaultTextRes: Int) {
    textView(textViewId).apply {
      apply(AccentTextView)
      text(defaultTextRes)
    }
  }
  
  private fun showAccentTextViewError(textViewId: Int, @StringRes errorTextRes: Int) {
    textView(textViewId).apply {
      textColor(Colors.Error)
      text(errorTextRes)
    }
  }
  
  companion object {
  
    val PlainTextScreenRoot = View.generateViewId()
    val ImageBack = View.generateViewId()
    val MainTitle = View.generateViewId()
    val ImageDelete = View.generateViewId()
    val Title = View.generateViewId()
    val EditTextTitle = View.generateViewId()
    val ImageTitleAction = View.generateViewId()
    val Text = View.generateViewId()
    val EditTextText = View.generateViewId()
    val ImageTextAction = View.generateViewId()
    val ButtonSave = View.generateViewId()
  }
}
