package com.arsvechkarev.vault.test.features.main_list

import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.common.base.BaseScreen
import io.github.kakaocup.kakao.text.KTextView

object KMainListScreen : BaseScreen<KMainListScreen>() {
  
  override val viewClass = MainListScreen::class.java
  
  val title = KTextView { withText("Vault") }
}
