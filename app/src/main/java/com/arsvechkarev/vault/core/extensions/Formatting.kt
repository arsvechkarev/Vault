package com.arsvechkarev.vault.core.extensions

import android.text.SpannableString
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.TypefaceSpan
import com.arsvechkarev.vault.core.navigation.Screen
import com.arsvechkarev.vault.viewbuilding.Fonts

fun Screen.getDeleteMessageText(serviceName: String): CharSequence {
  val questionPrefixLength = getString(R.string.text_delete_service_question_prefix).length
  val questionString = getString(R.string.text_delete_service_question, serviceName)
  val spannableString = SpannableString(questionString)
  spannableString.setSpan(TypefaceSpan(Fonts.SegoeUiBold), questionPrefixLength + 1,
    questionString.length - 1, 0)
  return spannableString
}