package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.ButtonContinue
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.EditTextLogin
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.EditTextWebsiteName
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.TextLogin
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.TextWebsiteName
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.Toolbar
import com.arsvechkarev.vault.test.core.base.BaseScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KCreatingPasswordEntryScreen : BaseScreen<KCreatingPasswordEntryScreen>() {
  
  override val viewClass = CreatingPasswordEntryScreen::class.java
  
  val iconBack = KImageView { withDrawable(R.drawable.ic_back) }
  val title = KTextView {
    withParent { withId(Toolbar) }
    withText("New password")
  }
  val textWebsiteName = KTextView { withId(TextWebsiteName) }
  val editTextWebsiteName = KEditText { withId(EditTextWebsiteName) }
  val textLogin = KTextView { withId(TextLogin) }
  val editTextLogin = KEditText { withId(EditTextLogin) }
  val buttonContinue = KTextView { withId(ButtonContinue) }
}
