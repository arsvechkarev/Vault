package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.ButtonSave
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.EditTextText
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.ImageTextAction
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.ImageTitleAction
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.MainTitle
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.NoteScreenRoot
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.Title
import com.arsvechkarev.vault.features.note_entry.NoteEntryScreen.Companion.TitleText
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KNoteEntryScreen : BaseScreen<KNoteEntryScreen>() {
  
  override val viewClass = NoteEntryScreen::class.java
  
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val textMainTitle = KTextView { withId(MainTitle) }
  val imageDelete = KImageView { withId(ImageDelete) }
  val title = KTextView { withId(Title) }
  val editTextTitle = KEditText { withId(EditTextTitle) }
  val imageTitleAction = KImageView { withId(ImageTitleAction) }
  val titleText = KTextView { withId(TitleText) }
  val editTextText = KEditText { withId(EditTextText) }
  val imageTextAction = KImageView { withId(ImageTextAction) }
  val buttonSave = KTextView { withId(ButtonSave) }
  val confirmationDialog = KInfoDialog(NoteScreenRoot)
  val snackbar = KSnackbar()
}
