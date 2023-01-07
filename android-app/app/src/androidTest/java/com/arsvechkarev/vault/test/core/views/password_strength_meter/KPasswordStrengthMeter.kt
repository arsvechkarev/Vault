package com.arsvechkarev.vault.test.core.views.password_strength_meter

import com.arsvechkarev.vault.core.views.PasswordStrengthMeter
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.common.views.KBaseView

class KPasswordStrengthMeter : KBaseView<KEditTextPassword>({
  isInstanceOf(PasswordStrengthMeter::class.java)
}), KPasswordStrengthAssertions
