package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ButtonImportPasswords
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ImageClearKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ImportPasswordsScreenRoot
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TextSelectKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TextSelectPasswordsFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TitleSelectKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.TitleSelectPasswordsFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ViewSelectKeyFile
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen.Companion.ViewSelectPasswordsFile
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KEnterPasswordDialog
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KImportPasswordsScreen : BaseScreen<KImportPasswordsScreen>() {
  
  override val viewClass = ImportPasswordsScreen::class.java
  
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val titleSelectPasswordsFile = KTextView { withId(TitleSelectPasswordsFile) }
  val textSelectPasswordsFile = KTextView { withId(TextSelectPasswordsFile) }
  val viewSelectPasswordsFile = KView { withId(ViewSelectPasswordsFile) }
  val titleSelectKeyFile = KTextView { withId(TitleSelectKeyFile) }
  val textSelectKeyFile = KTextView { withId(TextSelectKeyFile) }
  val imageClearKeyFile = KImageView { withId(ImageClearKeyFile) }
  val viewSelectKeyFile = KView { withId(ViewSelectKeyFile) }
  val buttonImportPasswords = KTextView { withId(ButtonImportPasswords) }
  val infoDialog = KInfoDialog(ImportPasswordsScreenRoot)
  val enterPasswordDialog = KEnterPasswordDialog()
}
