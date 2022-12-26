package com.arsvechkarev.vault.test.core.views.edit_text_password

import android.widget.EditText
import android.widget.ImageView
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView

class KEditTextPassword(
  builder: ViewBuilder.() -> Unit
) : KBaseView<KEditTextPassword>(builder), KEditTextPasswordAssertions {
  
  private val editText = KEditText {
    withParent(builder)
    isAssignableFrom(EditText::class.java)
  }
  
  private val imageVisibility = KImageView {
    withParent(builder)
    isAssignableFrom(ImageView::class.java)
  }
  
  fun typeText(text: String) = editText.typeText(text)
  
  fun replaceText(text: String) = editText.replaceText(text)
  
  fun clearText() = editText.clearText()
  
  fun hasText(text: String) = editText.hasText(text)
  
  fun hasHint(text: String) = editText.hasHint(text)
  
  fun hasEmptyText() = editText.hasText("")
  
  fun toggleVisibility() = imageVisibility.click()
}
