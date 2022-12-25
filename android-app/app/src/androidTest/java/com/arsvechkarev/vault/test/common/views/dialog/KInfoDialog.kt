package com.arsvechkarev.vault.test.common.views.dialog

import com.arsvechkarev.vault.features.common.dialogs.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoMessage
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText1
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText2
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoTitle
import com.arsvechkarev.vault.test.common.utils.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KInfoDialog : KBaseView<KInfoDialog>({ withClassNameTag<InfoDialog>() }) {
  
  val title = KTextView { withId(DialogInfoTitle) }
  val message = KTextView { withId(DialogInfoMessage) }
  val text1 = KTextView { withId(DialogInfoText1) }
  val text2 = KTextView { withId(DialogInfoText2) }
  
  fun isShown() {
    title.isDisplayed()
    message.isDisplayed()
  }
  
  fun isNotShown() {
    title.isNotDisplayed()
    message.isNotDisplayed()
  }
  
  fun hide() {
    text1.click()
  }
}
