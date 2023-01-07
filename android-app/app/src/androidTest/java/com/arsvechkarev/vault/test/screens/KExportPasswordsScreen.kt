package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.ButtonExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.EditTextFilename
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.ExportPasswordsScreenRoot
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.LayoutFolder
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.TextFolder
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.TitleFilename
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen.Companion.TitleFolder
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KExportPasswordsScreen : BaseScreen<KExportPasswordsScreen>() {
  
  override val viewClass = ExportPasswordsScreen::class.java
  
  val iconBack = KImageView { withDrawable(R.drawable.ic_back) }
  val layoutFolder = KView { withId(LayoutFolder) }
  val titleFolder = KTextView { withId(TitleFolder) }
  val textFolder = KTextView { withId(TextFolder) }
  val titleFilename = KTextView { withId(TitleFilename) }
  val editTextFilename = KEditText { withId(EditTextFilename) }
  val buttonExportPasswords = KTextView { withId(ButtonExportPasswords) }
  val successDialog = KInfoDialog(parentId = ExportPasswordsScreenRoot)
}
