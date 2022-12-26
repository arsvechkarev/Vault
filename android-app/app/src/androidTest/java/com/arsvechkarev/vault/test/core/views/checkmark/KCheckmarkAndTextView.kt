package com.arsvechkarev.vault.test.core.views.checkmark

import android.widget.TextView
import com.arsvechkarev.vault.core.views.Checkmark
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView

class KCheckmarkAndTextView(
  builder: ViewBuilder.() -> Unit
) : KBaseView<KCheckmarkAndTextView>(builder) {
  
  private val checkmark = KCheckmark {
    withParent(builder)
    isInstanceOf(Checkmark::class.java)
  }
  
  private val textView = KTextView {
    withParent(builder)
    isInstanceOf(TextView::class.java)
  }
  
  fun isChecked() = checkmark.isChecked()
  
  fun isNotChecked() = checkmark.isNotChecked()
  
  fun hasText(text: String) = textView.hasText(text)
}
