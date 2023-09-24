package com.arsvechkarev.vault.test.screens

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.settings.SettingsScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen.Companion.ItemChangePassword
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KEnterPasswordDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView

object KSettingsScreen : BaseScreen<KSettingsScreen>() {
  
  override val viewClass = SettingsScreen::class.java
  
  val iconBack = KImageView { withId(R.drawable.ic_back) }
  val itemChangePassword = KView { withId(ItemChangePassword) }
  val enterPasswordDialog = KEnterPasswordDialog()
  val snackbar = KSnackbar()
}
