package com.arsvechkarev.vault.test.features.info

import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ImageDelete
import com.arsvechkarev.vault.test.common.base.BaseScreen
import com.arsvechkarev.vault.test.common.views.dialog.KInfoDialog
import io.github.kakaocup.kakao.image.KImageView

object KInfoScreen : BaseScreen<KInfoScreen>() {
  
  override val viewClass = InfoScreen::class.java
  
  val iconDelete = KImageView { withId(ImageDelete) }
  
  val confirmationDialog = KInfoDialog()
}
