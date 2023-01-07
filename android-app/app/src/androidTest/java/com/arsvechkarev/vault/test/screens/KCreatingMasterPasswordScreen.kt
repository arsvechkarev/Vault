package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.EditTextEnterPassword
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.EditTextRepeatPassword
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.ImageErrorQuestion
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.RepeatPasswordLayout
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.TextContinue
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.TextError
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.TextPasswordStrength
import com.arsvechkarev.vault.features.creating_master_password.CreatingMasterPasswordScreen.Companion.TextTitle
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KPasswordStrengthDialog
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import com.arsvechkarev.vault.test.core.views.password_strength_meter.KPasswordStrengthMeter
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KCreatingMasterPasswordScreen : BaseScreen<KCreatingMasterPasswordScreen>() {
  
  override val viewClass = CreatingMasterPasswordScreen::class.java
  
  val title = KTextView { withId(TextTitle) }
  val iconBack = KImageView { withDrawable(R.drawable.ic_back) }
  val editTextEnterPassword = KEditTextPassword { withId(EditTextEnterPassword) }
  val editTextRepeatPassword = KEditTextPassword { withId(EditTextRepeatPassword) }
  val textPasswordStrength = KTextView { withId(TextPasswordStrength) }
  val passwordStrengthMeter = KPasswordStrengthMeter()
  val textError = KTextView { withId(TextError) }
  val iconError = KImageView { withId(ImageErrorQuestion) }
  val buttonContinue = KTextView {
    withId(TextContinue)
    withText("Continue")
  }
  val repeatPasswordLayout = KView { withId(RepeatPasswordLayout) }
  val repeatPasswordTitle = KTextView {
    withParent { withId(RepeatPasswordLayout) }
    withText("Repeat password")
  }
  
  val passwordStrengthDialog = KPasswordStrengthDialog()
}
