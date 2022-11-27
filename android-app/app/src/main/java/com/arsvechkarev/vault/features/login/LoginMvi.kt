package com.arsvechkarev.vault.features.login

sealed interface LoginEvent {
  object ShowSuccessCheckingPassword : LoginEvent
  object ShowFailureCheckingPassword : LoginEvent
}

sealed interface LoginUiEvent : LoginEvent {
  object OnAppearedOnScreen : LoginUiEvent
  class OnEnteredPassword(val password: String) : LoginUiEvent
  object OnTypingText : LoginUiEvent
}

sealed interface LoginCommand {
  class EnterWithMasterPassword(val password: String) : LoginCommand
  object GoToMainListScreen : LoginCommand
}

data class LoginState(
  val isLoading: Boolean = false,
  val showPasswordIsIncorrect: Boolean = false,
  val showKeyboard: Boolean = false,
)
