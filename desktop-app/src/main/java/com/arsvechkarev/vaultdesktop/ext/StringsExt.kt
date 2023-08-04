package com.arsvechkarev.vaultdesktop.ext

import java.util.Locale

fun String.firstCapitalizedLetter(): String {
  return this[0].toString().replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
  }
}
