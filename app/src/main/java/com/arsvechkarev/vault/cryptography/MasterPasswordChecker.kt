package com.arsvechkarev.vault.cryptography

interface MasterPasswordChecker {
  
  fun encodeSecretPhrase(masterPassword: String)
  
  fun isCorrect(masterPassword: String): Boolean
}