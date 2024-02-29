package com.arsvechkarev.vault.test.core.views.dialog

import com.arsvechkarev.vault.core.views.EditTextPassword
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.ButtonContinue
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.ImageCrossId
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.TextError
import com.arsvechkarev.vault.features.common.dialogs.EnterPasswordDialog.Companion.Title
import com.arsvechkarev.vault.test.core.ext.withClassNameTag
import com.arsvechkarev.vault.test.core.views.edit_text_password.KEditTextPassword
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KTextView

class KEnterPasswordDialog :
  KBaseView<KEnterPasswordDialog>({ withClassNameTag<EnterPasswordDialog>() }) {
  
  val title = KTextView { withId(Title) }
  val imageCross = KImageView { withId(ImageCrossId) }
  val editText = KEditTextPassword {
    isDescendantOfA { withClassNameTag<EnterPasswordDialog>() }
    isInstanceOf(EditTextPassword::class.java)
  }
  val textError = KTextView { withId(TextError) }
  val buttonContinue = KTextView { withId(ButtonContinue) }
}
