package com.arsvechkarev.vault.core.extensions

import com.arsvechkarev.vault.core.SPECIAL_SYMBOLS_STRING

val String.hasUppercaseLetters get() = matches(Regex(".*[A-Z]+.*"))

val String.hasLowercaseLetters get() = matches(Regex(".*[a-z]+.*"))

val String.hasNumbers get() = matches(Regex(".*\\d+.*"))

val String.hasSpecialSymbols get() = matches(Regex(".*[$SPECIAL_SYMBOLS_STRING]+.*"))