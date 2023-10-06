package com.arsvechkarev.vault.features.login

import domain.Password

sealed interface LoginEvent {
  object ShowSuccessCheckingPassword : LoginEvent
  object ShowFailureCheckingPassword : LoginEvent
}

sealed interface LoginUiEvent : LoginEvent {
  class OnEnteredPassword(val password: Password) : LoginUiEvent
  object OnTypingText : LoginUiEvent
}

sealed interface LoginCommand {
  class EnterWithMasterPassword(val password: Password) : LoginCommand
  object GoToMainListScreen : LoginCommand
}

data class LoginState(
  val isLoading: Boolean = false,
  val showPasswordIsIncorrect: Boolean = false,
)
