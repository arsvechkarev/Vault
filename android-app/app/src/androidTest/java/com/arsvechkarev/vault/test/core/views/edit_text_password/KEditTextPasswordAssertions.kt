package com.arsvechkarev.vault.test.core.views.edit_text_password

import androidx.test.espresso.assertion.ViewAssertions
import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.test.core.base.baseMatcher
import io.github.kakaocup.kakao.common.assertions.BaseAssertions

interface KEditTextPasswordAssertions : BaseAssertions {
  
  fun isPasswordVisible() {
    view.check(ViewAssertions.matches(passwordVisibilityMatcher(isVisible = true)))
  }
  
  fun isPasswordHidden() {
    view.check(ViewAssertions.matches(passwordVisibilityMatcher(isVisible = false)))
  }
  
  private fun passwordVisibilityMatcher(isVisible: Boolean) = baseMatcher<EditTextPassword>(
    descriptionText = "password visible = $isVisible",
    matcher = { view -> view.isPasswordHidden != isVisible }
  )
}

