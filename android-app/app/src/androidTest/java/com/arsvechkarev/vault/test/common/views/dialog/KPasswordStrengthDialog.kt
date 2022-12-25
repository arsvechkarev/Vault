package com.arsvechkarev.vault.test.common.views.dialog

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.dialogs.PasswordStrengthDialog
import com.arsvechkarev.vault.test.common.ext.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KPasswordStrengthDialog :
  KBaseView<KInfoDialog>({ withClassNameTag<PasswordStrengthDialog>() }) {
  
  private val title = KTextView { withText("Password strength") }
  private val description = KTextView { withText(R.string.text_password_should_be_strong) }
  private val textGotIt = KTextView { withText("GOT IT") }
  
  fun isShown() {
    title.isDisplayed()
    description.isDisplayed()
    textGotIt.isDisplayed()
  }
  
  fun isNotShown() {
    title.isNotDisplayed()
    description.isNotDisplayed()
    textGotIt.isNotDisplayed()
  }
  
  fun hide() {
    textGotIt.click()
  }
}
