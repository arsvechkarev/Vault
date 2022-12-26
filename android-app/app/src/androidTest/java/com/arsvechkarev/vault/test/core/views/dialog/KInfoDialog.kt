package com.arsvechkarev.vault.test.core.views.dialog

import com.arsvechkarev.vault.features.common.dialogs.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoMessage
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText1
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText2
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoTitle
import com.arsvechkarev.vault.test.core.base.baseAction
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KInfoDialog : KBaseView<KInfoDialog>({ withClassNameTag<InfoDialog>() }) {
  
  val title = KTextView { withId(DialogInfoTitle) }
  val message = KTextView { withId(DialogInfoMessage) }
  val action1 = KTextView { withId(DialogInfoText1) }
  val action2 = KTextView { withId(DialogInfoText2) }
  
//  fun isShown() {
//    title.isDisplayed()
//    message.isDisplayed()
//  }
//
//  fun isHidden() {
//    title.isNotDisplayed()
//    message.isNotDisplayed()
//  }
  
  fun hide() {
    view.perform(
      baseAction<InfoDialog>(
        description = "hiding info dialog",
        action = { _, view -> view.hide() }
      )
    )
  }
}
