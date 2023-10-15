package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.features.common.biometrics.BiometricsCryptography
import domain.Password

sealed interface LoginEvent {
  object ShowLoginSuccess : LoginEvent
  object ShowFailureCheckingPassword : LoginEvent
  object ShowFailureCheckingBiometrics : LoginEvent
  class BiometricsEnterPossible(val iv: ByteArray) : LoginEvent
  object BiometricsEnterNotPossible : LoginEvent
}

sealed interface LoginUiEvent : LoginEvent {
  object OnInit : LoginUiEvent
  class OnEnterWithPassword(val password: Password) : LoginUiEvent
  class OnEnterWithBiometrics(val cryptography: BiometricsCryptography) : LoginUiEvent
  object OnTypingText : LoginUiEvent
}

sealed interface LoginCommand {
  object GetBiometricsEnterPossible : LoginCommand
  class EnterWithMasterPassword(val password: Password) : LoginCommand
  class EnterWithBiometrics(val cryptography: BiometricsCryptography) : LoginCommand
  object GoToMainListScreen : LoginCommand
}

sealed interface LoginNews {
  class ShowBiometricsPrompt(val iv: ByteArray) : LoginNews
}

data class LoginState(
  val showLoading: Boolean = false,
  val showPasswordIsIncorrect: Boolean = false,
)
