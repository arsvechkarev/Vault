package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.login.LoginScreen.Companion.ButtonContinue
import com.arsvechkarev.vault.features.login.LoginScreen.Companion.EditTextPassword
import com.arsvechkarev.vault.features.login.LoginScreen.Companion.TextError
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.text.KTextView

object KLoginScreen : BaseScreen<KLoginScreen>() {
  
  override val viewClass = LoginScreen::class.java
  
  val editTextEnterPassword = KEditTextPassword { withId(EditTextPassword) }
  val buttonContinue = KTextView { withId(ButtonContinue) }
  val textError = KTextView { withId(TextError) }
}
