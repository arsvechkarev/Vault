package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.PasswordConfigurationMode
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
  class Setup(val mode: PasswordConfigurationMode) : CreatingPasswordUiEvent
  object SetupCompleted : CreatingPasswordUiEvent
  class OnPasswordChanged(val password: Password) : CreatingPasswordUiEvent
  object OnToggledUppercaseSymbols : CreatingPasswordUiEvent
  object OnToggledNumbers : CreatingPasswordUiEvent
  object OnToggledSpecialSymbols : CreatingPasswordUiEvent
  class OnPasswordLengthChanged(val length: Int) : CreatingPasswordUiEvent
  object OnGeneratePasswordClicked : CreatingPasswordUiEvent
  object OnSavePasswordClicked : CreatingPasswordUiEvent
  object OnBackClicked : CreatingPasswordUiEvent
  object OnConfirmPasswordSavingClicked : CreatingPasswordUiEvent
  object OnDeclinePasswordSavingClicked : CreatingPasswordUiEvent
}

sealed interface CreatingPasswordCommand {
  class CheckPasswordStrength(val password: Password) : CreatingPasswordCommand
  class ComputePasswordCharacteristics(val password: Password) : CreatingPasswordCommand
  class GeneratePassword(
    val length: Int,
    val characteristics: EnumSet<PasswordCharacteristic>
  ) : CreatingPasswordCommand
  
  class ConfirmSavePassword(val password: Password) : CreatingPasswordCommand
  object GoBack : CreatingPasswordCommand
}

sealed interface CreatingPasswordNews {
  class ShowGeneratedPassword(val password: Password) : CreatingPasswordNews
}

data class CreatingPasswordState(
  val mode: PasswordConfigurationMode? = null,
  val setupCompleted: Boolean = false,
  val password: Password = Password.empty(),
  val uppercaseSymbolsEnabled: Boolean = true,
  val numbersEnabled: Boolean = true,
  val specialSymbolsEnabled: Boolean = true,
  val passwordLength: Int = DEFAULT_PASSWORD_LENGTH,
  val showConfirmationDialog: Boolean = false,
  val passwordStrength: PasswordStrength? = null,
)
