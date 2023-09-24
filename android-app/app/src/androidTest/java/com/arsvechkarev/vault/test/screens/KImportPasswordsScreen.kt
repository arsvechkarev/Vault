package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ButtonImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ImportPasswordsScreenRoot
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.LayoutSelectFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TextSelectFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TitleSelectFile
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KEnterPasswordDialog
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KImportPasswordsScreen : BaseScreen<KImportPasswordsScreen>() {
  
  override val viewClass = ImportPasswordsScreen::class.java
  
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val layoutSelectFile = KView { withId(LayoutSelectFile) }
  val titleSelectFile = KTextView { withId(TitleSelectFile) }
  val textSelectFile = KTextView { withId(TextSelectFile) }
  val buttonImportPasswords = KTextView { withId(ButtonImportPasswords) }
  val infoDialog = KInfoDialog(ImportPasswordsScreenRoot)
  val enterPasswordDialog = KEnterPasswordDialog()
}
