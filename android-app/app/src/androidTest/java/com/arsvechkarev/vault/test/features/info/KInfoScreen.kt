package com.arsvechkarev.vault.test.features.info

import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextLogin
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextNotes
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.EditTextWebsiteName
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.IconDelete
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.ImageWebsite
import com.arsvechkarev.vault.features.info.InfoScreen.Companion.TextWebsiteName
import com.arsvechkarev.vault.test.core.base.BaseScreen
import com.arsvechkarev.vault.test.core.views.dialog.KInfoDialog
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

object KInfoScreen : BaseScreen<KInfoScreen>() {
  
  override val viewClass = InfoScreen::class.java
  
  val iconDelete = KImageView { withId(IconDelete) }
  val imageWebsite = KImageView { withId(ImageWebsite) }
  val textWebsiteName = KTextView { withId(TextWebsiteName) }
  val editTextWebsiteName = KEditText { withId(EditTextWebsiteName) }
  val editTextLogin = KEditText { withId(EditTextLogin) }
  val editTextNotes = KEditText { withId(EditTextNotes) }
  val confirmationDialog = KInfoDialog()
}
