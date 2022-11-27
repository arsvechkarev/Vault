package com.arsvechkarev.vault.features.creating_password

import buisnesslogic.DEFAULT_PASSWORD_LENGTH
import buisnesslogic.PasswordStrength
import buisnesslogic.model.PasswordCharacteristic
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordReceiveEvent.Setup.PasswordConfigurationMode
import java.util.EnumSet

sealed interface CreatingPasswordEvent {
  class GeneratedPassword(val password: String) : CreatingPasswordEvent
  class Setup(val mode: PasswordConfigurationMode) : CreatingPasswordEvent
  class PasswordStrengthChanged(val strength: PasswordStrength?) : CreatingPasswordEvent
  class ComputedPasswordCharacteristics(
    val characteristics: EnumSet<PasswordCharacteristic>
  ) : CreatingPasswordEvent
}

sealed interface CreatingPasswordUiEvent : CreatingPasswordEvent {
  object SetupCompleted : CreatingPasswordUiEvent
  class OnPasswordChanged(val password: String) : CreatingPasswordUiEvent
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
  class CheckPasswordStrength(val password: String) : CreatingPasswordCommand
  class ComputePasswordCharacteristics(val password: String) : CreatingPasswordCommand
  class GeneratePassword(
    val length: Int,
    val characteristics: EnumSet<PasswordCharacteristic>
  ) : CreatingPasswordCommand
  
  class ConfirmSavePassword(val password: String) : CreatingPasswordCommand
  object GoBack : CreatingPasswordCommand
}

sealed interface CreatingPasswordNews {
  class ShowGeneratedPassword(val password: String) : CreatingPasswordNews
}

data class CreatingPasswordState(
  val mode: PasswordConfigurationMode? = null,
  val setupCompleted: Boolean = false,
  val password: String = "",
  val showPasswordCantBeEmpty: Boolean = false,
  val uppercaseSymbolsEnabled: Boolean = true,
  val numbersEnabled: Boolean = true,
  val specialSymbolsEnabled: Boolean = true,
  val passwordLength: Int = DEFAULT_PASSWORD_LENGTH,
  val showConfirmationDialog: Boolean = false,
  val passwordStrength: PasswordStrength? = null,
)
