package com.arsvechkarev.vault.test.core.views.dialog

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.common.dialogs.PasswordStrengthDialog
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KPasswordStrengthDialog :
  KBaseView<KInfoDialog>({ withClassNameTag<PasswordStrengthDialog>() }) {
  
  private val title = KTextView {
    withText("Password is too weak")
    withSibling { withText(R.string.text_password_should_be_strong) }
  }
  private val description = KTextView { withText(R.string.text_password_should_be_strong) }
  private val textContinueWithWeakPassword = KTextView { withText("CONTINUE WITH WEAK PASSWORD") }
  private val textChangePassword = KTextView { withText("CHANGE PASSWORD") }
  
  fun isShown() {
    title.isDisplayed()
    description.isDisplayed()
    textChangePassword.isDisplayed()
  }
  
  fun isNotShown() {
    title.isNotDisplayed()
    description.isNotDisplayed()
    textChangePassword.isNotDisplayed()
  }
  
  fun hide() {
    textChangePassword.click()
  }
  
  fun proceedWithWeakPassword() {
    textContinueWithWeakPassword.click()
  }
}
