package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextUsername
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageBack
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageCopyPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageEditPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageNotesAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageTitleAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageUsernameAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.PasswordInfoScreenRoot
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TextHiddenPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TextTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitlePassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleTitle
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleUsername
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KPasswordInfoScreen : BaseScreen<KPasswordInfoScreen>() {
  
  override val viewClass = PasswordInfoScreen::class.java
  
  val iconDelete = KImageView { withId(ImageDelete) }
  val iconBack = KImageView { withId(ImageBack) }
  val imageTitle = KImageView { withId(ImageTitle) }
  val titleTitle = KTextView { withId(TitleTitle) }
  val textTitle = KTextView { withId(TextTitle) }
  val editTextTitle = KEditText { withId(EditTextTitle) }
  val imageTitleAction = KImageView { withId(ImageTitleAction) }
  val titleUsername = KTextView { withId(TitleUsername) }
  val editTextUsername = KEditText { withId(EditTextUsername) }
  val imageUsernameAction = KImageView { withId(ImageUsernameAction) }
  val titlePassword = KTextView { withId(TitlePassword) }
  val textHiddenPassword = KTextView { withId(TextHiddenPassword) }
  val imageEditPassword = KImageView { withId(ImageEditPassword) }
  val imageCopyPassword = KImageView { withId(ImageCopyPassword) }
  val titleNotes = KTextView { withId(TitleNotes) }
  val editTextNotes = KEditText { withId(EditTextNotes) }
  val imageNotesAction = KImageView { withId(ImageNotesAction) }
  val snackbar = KSnackbar()
  val confirmationDialog = KInfoDialog(parentId = PasswordInfoScreenRoot)
}
