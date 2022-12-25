package com.arsvechkarev.vault.test.common.views.password_strength_meter

import com.arsvechkarev.vault.core.views.PasswordStrengthMeter
import com.arsvechkarev.vault.test.common.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.common.views.KBaseView

class KPasswordStrengthMeter : KBaseView<KEditTextPassword>({
  withTag(PasswordStrengthMeter::class.java.name)
  isAssignableFrom(PasswordStrengthMeter::class.java)
}), KPasswordStrengthAssertions
