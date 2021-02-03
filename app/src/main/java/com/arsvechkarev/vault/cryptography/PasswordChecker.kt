package com.arsvechkarev.vault.cryptography

interface PasswordChecker {
  
  fun checkStrength(password: String): PasswordStrength?
  
  fun validate(password: String): PasswordStatus
}