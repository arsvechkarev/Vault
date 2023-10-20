package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ButtonSave
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.EditTextNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.EditTextUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.EditTextUsername
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageBack
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageCopyPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageEditPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageFavorite
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageNotesAction
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageOpenUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageTitleAction
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageUrlAction
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.ImageUsernameAction
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.PasswordEntryScreenRoot
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TextPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitleNewPassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitleNotes
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitlePassword
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitleTitle
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitleUrl
import com.arsvechkarev.vault.features.password_entry.PasswordEntryScreen.Companion.TitleUsername
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KPasswordEntryScreen : BaseScreen<KPasswordEntryScreen>() {
  
  override val viewClass = PasswordEntryScreen::class.java
  
  val imageBack = KImageView { withId(ImageBack) }
  val imageDelete = KImageView { withId(ImageDelete) }
  val imageFavorite = KImageView { withId(ImageFavorite) }
  val titleNewPassword = KTextView { withId(TitleNewPassword) }
  val imageTitle = KImageView { withId(ImageTitle) }
  val titleTitle = KTextView { withId(TitleTitle) }
  val editTextTitle = KEditText { withId(EditTextTitle) }
  val imageTitleAction = KImageView { withId(ImageTitleAction) }
  val titleUsername = KTextView { withId(TitleUsername) }
  val editTextUsername = KEditText { withId(EditTextUsername) }
  val imageUsernameAction = KImageView { withId(ImageUsernameAction) }
  val titlePassword = KTextView { withId(TitlePassword) }
  val textPassword = KTextView { withId(TextPassword) }
  val imageEditPassword = KImageView { withId(ImageEditPassword) }
  val imageCopyPassword = KImageView { withId(ImageCopyPassword) }
  val titleUrl = KTextView { withId(TitleUrl) }
  val editTextUrl = KEditText { withId(EditTextUrl) }
  val imageOpenUrl = KImageView { withId(ImageOpenUrl) }
  val imageUrlAction = KImageView { withId(ImageUrlAction) }
  val titleNotes = KTextView { withId(TitleNotes) }
  val editTextNotes = KEditText { withId(EditTextNotes) }
  val imageNotesAction = KImageView { withId(ImageNotesAction) }
  val buttonSave = KImageView { withId(ButtonSave) }
  val snackbar = KSnackbar()
  val confirmationDialog = KInfoDialog(PasswordEntryScreenRoot)
}
