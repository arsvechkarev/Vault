package com.arsvechkarev.vault.password

interface MasterPasswordChecker {
  
  fun encodeSecretPhrase(masterPassword: String)
  
  fun isCorrect(masterPassword: String): Boolean
}