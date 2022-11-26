package com.arsvechkarev.vault.features.creating_password

sealed interface CreatingPasswordReceiveEvent {
  
  class Setup(val mode: PasswordConfigurationMode) : CreatingPasswordReceiveEvent {
    
    sealed interface PasswordConfigurationMode {
      object NewPassword : PasswordConfigurationMode
      class EditPassword(val password: String) : PasswordConfigurationMode
    }
  }
}

sealed interface CreatingPasswordSendEvent {
  class OnSavingPassword(val password: String) : CreatingPasswordSendEvent
}