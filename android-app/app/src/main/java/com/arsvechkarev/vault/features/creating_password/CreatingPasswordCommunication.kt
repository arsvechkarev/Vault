package com.arsvechkarev.vault.features.creating_password

import buisnesslogic.Password

class CreatingPasswordReceiveEvent(val mode: PasswordConfigurationMode) {
  
  sealed interface PasswordConfigurationMode {
    object NewPassword : PasswordConfigurationMode
    class EditPassword(val password: Password) : PasswordConfigurationMode
  }
}

class CreatingPasswordSendEvent(val password: Password)
