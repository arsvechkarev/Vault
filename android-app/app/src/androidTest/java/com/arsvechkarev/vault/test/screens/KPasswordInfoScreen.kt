package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.EditTextWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageBack
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageCopyPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageDelete
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageEditPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageLoginAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageNotesAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageWebsite
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.ImageWebsiteNameAction
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.PasswordInfoScreenRoot
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TextHiddenPassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TextWebsiteName
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleLogin
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleNotes
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitlePassword
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen.Companion.TitleWebsiteName
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
  val imageWebsite = KImageView { withId(ImageWebsite) }
  val textWebsiteName = KTextView { withId(TextWebsiteName) }
  val titleWebsiteName = KTextView { withId(TitleWebsiteName) }
  val editTextWebsiteName = KEditText { withId(EditTextWebsiteName) }
  val imageWebsiteNameAction = KImageView { withId(ImageWebsiteNameAction) }
  val titleLogin = KTextView { withId(TitleLogin) }
  val editTextLogin = KEditText { withId(EditTextLogin) }
  val imageLoginAction = KImageView { withId(ImageLoginAction) }
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
