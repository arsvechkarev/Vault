package com.arsvechkarev.vault.cryptography.generator

import com.arsvechkarev.vault.core.model.PasswordCharacteristics

interface PasswordGenerator {
  
  fun generatePassword(length: Int, characteristics: Collection<PasswordCharacteristics>): String
}