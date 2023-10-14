package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.features.common.domain.CreatingPasswordMode
import domain.DEFAULT_PASSWORD_LENGTH
import domain.Password
import domain.PasswordStrength
import domain.model.PasswordCharacteristic
import java.util.EnumSet

sealed interface CreatingPasswordEvent {
  class GeneratedPassword(val password: Password) : CreatingPasswordEvent
  class PasswordStrengthChanged(val strength: PasswordStrength?) : CreatingPasswordEvent
  class ComputedPasswordCharacteristics(
    val characteristics: EnumSet<PasswordCharacteristic>
  ) : CreatingPasswordEvent
}

sealed interface CreatingPasswordUiEvent : CreatingPasswordEvent {
  class Setup(val configuration: CreatingPasswordMode) : CreatingPasswordUiEvent
  object SetupCompleted : CreatingPasswordUiEvent
  class OnPasswordChanged(val password: Password) : CreatingPasswordUiEvent
  object OnToggledUppercaseSymbols : CreatingPasswordUiEvent
  object OnToggledNumbers : CreatingPasswordUiEvent
  object OnToggledSpecialSymbols : CreatingPasswordUiEvent
  class OnPasswordLengthChanged(val length: Int) : CreatingPasswordUiEvent
  object OnGeneratePasswordClicked : CreatingPasswordUiEvent
  object OnSavePasswordClicked : CreatingPasswordUiEvent
  object OnBackPressed : CreatingPasswordUiEvent
}

sealed interface CreatingPasswordCommand {
  class CheckPasswordStrength(val password: Password) : CreatingPasswordCommand
  class ComputePasswordCharacteristics(val password: Password) : CreatingPasswordCommand
  class GeneratePassword(
    val length: Int,
    val characteristics: EnumSet<PasswordCharacteristic>
  ) : CreatingPasswordCommand
  
  class SendPasswordChangeEvent(val password: Password) : CreatingPasswordCommand
  object GoBack : CreatingPasswordCommand
}

sealed interface CreatingPasswordNews {
  class SetupExistingPassword(val password: Password) : CreatingPasswordNews
  object SetupNewPassword : CreatingPasswordNews
  class ShowGeneratedPassword(val password: Password) : CreatingPasswordNews
}

data class CreatingPasswordState(
  val mode: CreatingPasswordMode? = null,
  val password: Password = Password.empty(),
  val uppercaseSymbolsEnabled: Boolean = true,
  val numbersEnabled: Boolean = true,
  val specialSymbolsEnabled: Boolean = true,
  val passwordLength: Int = DEFAULT_PASSWORD_LENGTH,
  val passwordStrength: PasswordStrength? = null,
)
