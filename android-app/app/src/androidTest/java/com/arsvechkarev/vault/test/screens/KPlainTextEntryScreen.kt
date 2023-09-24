package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.ButtonSave
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.EditTextText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.ImageTextAction
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.ImageTitleAction
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.MainTitle
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.PlainTextScreenRoot
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.Title
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryScreen.Companion.TitleText
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KPlainTextEntryScreen : BaseScreen<KPlainTextEntryScreen>() {
  
  override val viewClass = PlainTextEntryScreen::class.java
  
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
  val confirmationDialog = KInfoDialog(PlainTextScreenRoot)
  val snackbar = KSnackbar()
}
