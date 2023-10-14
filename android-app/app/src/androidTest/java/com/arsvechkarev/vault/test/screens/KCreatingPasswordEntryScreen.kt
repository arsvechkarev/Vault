package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.test.core.base.BaseScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KCreatingPasswordEntryScreen : BaseScreen<KCreatingPasswordEntryScreen>() {
  
  override val viewClass = KCreatingPasswordEntryScreen::class.java
  
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val title = KTextView {
    //    withParent { withId(Toolbar) }
    withText("New password")
  }
  val textTitle = KTextView { }
  val editTextTitle = KEditText { }
  val textUsername = KTextView { }
  val editTextUsername = KEditText { }
  val buttonContinue = KTextView { }
}
