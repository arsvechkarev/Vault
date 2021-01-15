package com.arsvechkarev.vault.core.password

interface PasswordChecker {
  
  fun check(password: String): PasswordStrength?
}