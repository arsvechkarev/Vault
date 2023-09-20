package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.ButtonSave
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.EditTextText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.ImageTextAction
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.ImageTitleAction
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.MainTitle
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.PlainTextScreenRoot
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.Text
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen.Companion.Title
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KPlainTextEntryScreen : BaseScreen<KPlainTextEntryScreen>() {
  
  override val viewClass = PlainTextScreen::class.java
  
  val iconBack = KImageView { withDrawable(R.drawable.ic_back) }
  val textMainTitle = KTextView { withId(MainTitle) }
  val imageDelete = KImageView { withId(ImageDelete) }
  val textTitle = KTextView { withId(Title) }
  val editTextTitle = KEditText { withId(EditTextTitle) }
  val imageTitleAction = KImageView { withId(ImageTitleAction) }
  val textText = KTextView { withId(Text) }
  val editTextText = KEditText { withId(EditTextText) }
  val imageTextAction = KImageView { withId(ImageTextAction) }
  val buttonSave = KTextView { withId(ButtonSave) }
  val confirmationDialog = KInfoDialog(parentId = PlainTextScreenRoot)
  val snackbar = KSnackbar()
}
