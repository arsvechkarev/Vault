package com.arsvechkarev.vault.test.core.views.snackbar

import android.widget.TextView
import com.arsvechkarev.vault.core.views.AnimatableCheckmark
import com.arsvechkarev.vault.core.views.Snackbar
import com.arsvechkarev.vault.test.core.views.checkmark.KCheckmark
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KSnackbar : KBaseView<KSnackbar>({
  isInstanceOf(Snackbar::class.java)
}) {
  
  private val checkmark = KCheckmark {
    withParent { isInstanceOf(Snackbar::class.java) }
    isInstanceOf(AnimatableCheckmark::class.java)
  }
  
  private val textView = KTextView {
    withParent { isInstanceOf(Snackbar::class.java) }
    isInstanceOf(TextView::class.java)
  }
  
  fun isDisplayedWithText(text: String) {
    checkmark.isDisplayed()
    textView.hasText(text)
  }
}
