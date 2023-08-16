package com.arsvechkarev.vault.features.creating_password

class CreatingPasswordReceiveEvent(val mode: PasswordConfigurationMode) {
  
    sealed interface PasswordConfigurationMode {
      object NewPassword : PasswordConfigurationMode
      class EditPassword(val password: String) : PasswordConfigurationMode
    }
}

class CreatingPasswordSendEvent(val password: String)
