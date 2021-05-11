package com.arsvechkarev.vault.core.extensions

import android.widget.Toast
import androidx.annotation.StringRes
import navigation.BaseScreen

fun BaseScreen.showToast(@StringRes resId: Int) {
  Toast.makeText(contextNonNull, contextNonNull.getText(resId), Toast.LENGTH_LONG).show()
}