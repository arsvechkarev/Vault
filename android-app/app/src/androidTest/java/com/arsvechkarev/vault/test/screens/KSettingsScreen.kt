package com.arsvechkarev.vault.test.screens

import androidx.appcompat.widget.SwitchCompat
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.settings.SettingsScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen.Companion.ItemChangePassword
import com.arsvechkarev.vault.features.settings.SettingsScreen.Companion.ItemShowUsernames
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KEnterPasswordDialog
import com.arsvechkarev.vault.test.core.views.snackbar.KSnackbar
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.switch.KSwitch

object KSettingsScreen : BaseScreen<KSettingsScreen>() {
  
  override val viewClass = SettingsScreen::class.java
  
  val imageBack = KImageView { withDrawable(R.drawable.ic_back) }
  val itemChangePassword = KView { withId(ItemChangePassword) }
  val enterPasswordDialog = KEnterPasswordDialog()
  val switchShowUsernames = KSwitch {
    withParent { withId(ItemShowUsernames) }
    isInstanceOf(SwitchCompat::class.java)
  }
  val snackbar = KSnackbar()
}
