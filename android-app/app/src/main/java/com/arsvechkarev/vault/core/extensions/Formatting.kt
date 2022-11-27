package com.arsvechkarev.vault.core.extensions

import android.text.SpannableString
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewbuilding.Fonts
import com.arsvechkarev.vault.viewbuilding.TypefaceSpan
import navigation.BaseFragmentScreen

fun BaseFragmentScreen.getDeleteMessageText(serviceName: String): CharSequence {
  val questionPrefixLength = requireContext().getString(
    R.string.text_deleting_password_question_prefix
  ).length
  val questionString =
      requireContext().getString(R.string.text_deleting_password_question, serviceName)
  val spannableString = SpannableString(questionString)
  spannableString.setSpan(
    TypefaceSpan(Fonts.SegoeUiBold), questionPrefixLength + 1,
    questionString.length - 1, 0
  )
  return spannableString
}
