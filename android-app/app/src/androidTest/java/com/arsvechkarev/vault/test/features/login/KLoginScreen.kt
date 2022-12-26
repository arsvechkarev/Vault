package com.arsvechkarev.vault.test.features.login

import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.login.LoginScreen.Companion.EditTextPassword
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.text.KTextView

object KLoginScreen : BaseScreen<KLoginScreen>() {
  
  override val viewClass = LoginScreen::class.java
  
  val editTextEnterPassword = KEditTextPassword { withId(EditTextPassword) }
  val buttonContinue = KTextView { withId(LoginScreen.ButtonContinue) }
}
