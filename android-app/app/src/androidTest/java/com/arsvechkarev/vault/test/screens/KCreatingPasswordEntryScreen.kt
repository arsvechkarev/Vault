package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.ButtonContinue
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.EditTextTitle
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.EditTextUsername
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.TitleTitle
import com.arsvechkarev.vault.features.creating_password_entry.CreatingPasswordEntryScreen.Companion.TitleUsername
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
  val textTitle = KTextView { withId(TitleTitle) }
  val editTextTitle = KEditText { withId(EditTextTitle) }
  val textUsername = KTextView { withId(TitleUsername) }
  val editTextUsername = KEditText { withId(EditTextUsername) }
  val buttonContinue = KTextView { withId(ButtonContinue) }
}
