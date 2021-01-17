package com.arsvechkarev.vault.password

interface PasswordVerifier {
  
  fun check(password: String): PasswordStrength?
  
  fun validate(password: String): PasswordStatus
}