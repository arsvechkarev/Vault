package com.arsvechkarev.vault.test.features.info

import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ButtonCopyPassword
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ButtonEditPassword
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ButtonLoginAction
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ButtonNotesAction
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ButtonWebsiteNameAction
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextLogin
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextNotes
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.IconBack
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.IconDelete
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ImageWebsite
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.InfoScreenRoot
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TextHiddenPassword
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TextWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TitleLogin
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TitleNotes
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TitlePassword
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TitleWebsiteName
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KInfoScreen : BaseScreen<KInfoScreen>() {
  
  override val viewClass = InfoScreen::class.java
  
  val iconDelete = KImageView { withId(IconDelete) }
  val iconBack = KImageView { withId(IconBack) }
  val imageWebsite = KImageView { withId(ImageWebsite) }
  val textWebsiteName = KTextView { withId(TextWebsiteName) }
  val titleWebsiteName = KTextView { withId(TitleWebsiteName) }
  val editTextWebsiteName = KEditText { withId(EditTextWebsiteName) }
  val buttonWebsiteNameAction = KImageView { withId(ButtonWebsiteNameAction) }
  val titleLogin = KTextView { withId(TitleLogin) }
  val editTextLogin = KEditText { withId(EditTextLogin) }
  val buttonLoginAction = KImageView { withId(ButtonLoginAction) }
  val titlePassword = KTextView { withId(TitlePassword) }
  val textHiddenPassword = KTextView { withId(TextHiddenPassword) }
  val buttonEditPassword = KImageView { withId(ButtonEditPassword) }
  val buttonCopyPassword = KImageView { withId(ButtonCopyPassword) }
  val titleNotes = KTextView { withId(TitleNotes) }
  val editTextNotes = KEditText { withId(EditTextNotes) }
  val buttonNotesAction = KImageView { withId(ButtonNotesAction) }
  val snackbar = KSnackbar()
  val confirmationDialog = KInfoDialog(parentId = InfoScreenRoot)
}
