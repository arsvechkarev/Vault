package com.arsvechkarev.vault.test.features.initial

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.initial.InitialScreen
import com.arsvechkarev.vault.test.core.base.BaseScreen
import io.github.kakaocup.kakao.common.views.KView

object KInitialScreen : BaseScreen<KInitialScreen>() {
  
  override val viewClass = InitialScreen::class.java
  
  val buttonCreateMasterPassword = KView { withText(R.string.text_create_master_password) }
}
