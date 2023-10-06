package com.arsvechkarev.vault.test.core.views.password_strength_meter

import androidx.test.espresso.assertion.ViewAssertions
import com.arsvechkarev.vault.core.views.PasswordStrengthMeter
import com.arsvechkarev.vault.test.core.base.baseMatcher
import domain.PasswordStrength
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

interface KPasswordStrengthAssertions : BaseAssertions {
  
  fun isNotShowingStrength() {
    view.check(ViewAssertions.matches(passwordStrength(null)))
  }
  
  fun hasPasswordStrength(strength: PasswordStrength) {
    view.check(ViewAssertions.matches(passwordStrength(strength)))
  }
  
  private fun passwordStrength(strength: PasswordStrength?) = baseMatcher<PasswordStrengthMeter>(
    descriptionText = "with password strength $strength",
    matcher = { view -> view.strength == strength }
  )
}
