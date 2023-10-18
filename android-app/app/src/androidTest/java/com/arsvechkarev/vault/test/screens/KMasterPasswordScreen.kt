package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.TextWithQuestion
import com.arsvechkarev.vault.features.common.dialogs.DialogProgressBar
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen.Companion.EditTextEnterPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen.Companion.EditTextRepeatPassword
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen.Companion.MasterPasswordScreenRoot
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen.Companion.TextContinue
import com.arsvechkarev.vault.features.master_password.MasterPasswordScreen.Companion.TextPasswordStrength
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.dialog.KPasswordStrengthDialog
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import com.arsvechkarev.vault.test.core.views.password_strength_meter.KPasswordStrengthMeter
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KMasterPasswordScreen : BaseScreen<KMasterPasswordScreen>() {
  
  override val viewClass = MasterPasswordScreen::class.java
  
  val title = KTextView { withId(EditTextEnterPassword) }
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val editTextEnterPassword = KEditTextPassword { withId(EditTextEnterPassword) }
  val editTextRepeatPassword = KEditTextPassword { withId(EditTextRepeatPassword) }
  val textPasswordStrength = KTextView { withId(TextPasswordStrength) }
  val passwordStrengthMeter = KPasswordStrengthMeter()
  val textError = KTextView { withId(TextWithQuestion.Text) }
  val iconError = KImageView { withId(TextWithQuestion.Image) }
  val buttonContinue = KTextView {
    withId(TextContinue)
    withText("Continue")
  }
  val repeatPasswordLayout = KView { withId(EditTextEnterPassword) }
  val repeatPasswordTitle = KTextView {
    withParent { withId(EditTextEnterPassword) }
    withText("Repeat password")
  }
  val passwordStrengthDialog = KPasswordStrengthDialog()
  val confirmationDialog = KInfoDialog(MasterPasswordScreenRoot)
  val loadingDialog = KView {
    isDescendantOfA { withId(MasterPasswordScreenRoot) }
    withTag(DialogProgressBar)
  }
}
