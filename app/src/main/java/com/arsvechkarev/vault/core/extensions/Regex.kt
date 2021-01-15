package com.arsvechkarev.vault.core.extensions

val String.hasNumbers get() = matches(Regex(".*\\d+.*"))

val String.hasUppercaseLetters get() = matches(Regex(".*[A-Z]+.*"))

val String.hasLowercaseLetters get() = matches(Regex(".*[a-z]+.*"))

val String.hasSpecialSymbols get() = matches(Regex(".*[!@#$%^&*\\\\\"()_\\-=+/.><?,']+.*"))