package com.arsvechkarev.vault.features.password_checking

const val PasswordCheckingTag = "PasswordCheckingTag"

interface PasswordCheckingEvents

sealed class PasswordCheckingActions : PasswordCheckingEvents {
  
  object ShowDialog : PasswordCheckingActions()
  
  object HideDialog : PasswordCheckingActions()
}

sealed class PasswordCheckingReactions : PasswordCheckingEvents {
  
  object PasswordCheckedSuccessfully : PasswordCheckingReactions()
}