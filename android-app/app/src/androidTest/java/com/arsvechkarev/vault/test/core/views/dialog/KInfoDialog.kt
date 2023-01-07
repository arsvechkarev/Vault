package com.arsvechkarev.vault.test.core.views.dialog

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoMessage
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText1
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoText2
import com.arsvechkarev.vault.features.common.dialogs.InfoDialog.Companion.DialogInfoTitle
import com.arsvechkarev.vault.test.core.base.baseAction
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class KInfoDialog(
  parentId: Int,
  private val parent: Matcher<View> = ViewMatchers.withId(parentId)
) : KBaseView<KInfoDialog>({
  isDescendantOfA { withMatcher(parent) }
  withClassNameTag<InfoDialog>()
}) {
  
  val title = KTextView(parent) { withId(DialogInfoTitle) }
  val message = KTextView(parent) { withId(DialogInfoMessage) }
  val action1 = KTextView(parent) { withId(DialogInfoText1) }
  val action2 = KTextView(parent) { withId(DialogInfoText2) }
  
  fun hide() {
    view.perform(
      baseAction<InfoDialog>(
        description = "hiding info dialog",
        action = { _, view -> view.hide() }
      )
    )
  }
}
