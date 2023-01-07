package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen.Companion.ButtonChange
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen.Companion.ChangeMasterPasswordScreenRoot
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen.Companion.EditTextEnterNewPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen.Companion.EditTextRepeatPassword
import com.arsvechkarev.vault.features.change_master_password.ChangeMasterPasswordScreen.Companion.TextError
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KChangeMasterPasswordScreen : BaseScreen<KChangeMasterPasswordScreen>() {
  
  override val viewClass = ChangeMasterPasswordScreen::class.java
  
  val iconBack = KImageView {
    isDescendantOfA { withId(ChangeMasterPasswordScreenRoot) }
    withDrawable(R.drawable.ic_back)
  }
  val editTextEnterNewPassword = KEditTextPassword { withId(EditTextEnterNewPassword) }
  val editTextRepeatPassword = KEditTextPassword { withId(EditTextRepeatPassword) }
  val textError = KTextView { withId(TextError) }
  val buttonChange = KTextView { withId(ButtonChange) }
  val infoDialog = KInfoDialog(ChangeMasterPasswordScreenRoot)
}
