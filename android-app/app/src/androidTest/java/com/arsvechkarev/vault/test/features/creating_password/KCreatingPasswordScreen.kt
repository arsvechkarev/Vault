package com.arsvechkarev.vault.test.features.creating_password

import android.widget.SeekBar
import android.widget.TextView
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.PasswordStrengthMeterWithText
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen.Companion.EditTextPassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen.Companion.TextError
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen.Companion.TextPasswordLength
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen.Companion.Title
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import com.arsvechkarev.vault.test.core.views.checkmark.KCheckmarkAndTextView
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import com.arsvechkarev.vault.test.core.views.password_strength_meter.KPasswordStrengthMeter
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KSeekBar
import io.github.kakaocup.kakao.text.KTextView

object KCreatingPasswordScreen : BaseScreen<KCreatingPasswordScreen>() {
  
  override val viewClass = CreatingPasswordScreen::class.java
  
  val iconCross = KImageView {
    isDisplayed()
    withDrawable(R.drawable.ic_cross)
  }
  val title = KTextView { withId(Title) }
  val editTextPassword = KEditText { withId(EditTextPassword) }
  val textError = KTextView { withId(TextError) }
  val textPasswordStrength = KTextView {
    withParent { isInstanceOf(PasswordStrengthMeterWithText::class.java) }
    isInstanceOf(TextView::class.java)
  }
  val passwordStrengthMeter = KPasswordStrengthMeter()
  val textPasswordLength = KTextView { withId(TextPasswordLength) }
  val passwordLengthSpinner = KSeekBar { withClassNameTag<SeekBar>() }
  val checkmarkUppercaseSymbols = KCheckmarkAndTextView { withId(R.string.text_uppercase_symbols) }
  val checkmarkNumbers = KCheckmarkAndTextView { withId(R.string.text_numbers) }
  val buttonGeneratePassword = KTextView { withText("Generate password") }
  val buttonSavePassword = KTextView { withText("Save password") }
  val checkmarkSpecialSymbols = KCheckmarkAndTextView { withId(R.string.text_special_symbols) }
  val confirmationDialog = KInfoDialog()
}
