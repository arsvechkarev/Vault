package com.arsvechkarev.vault.cryptography

interface MasterPasswordChecker {
  
  fun initializeEncryptedFile(masterPassword: String)
  
  fun isCorrect(masterPassword: String): Boolean
}