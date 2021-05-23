package com.arsvechkarev.vault.features.start

sealed class StartScreenAction {
  
  object ShowFingerprintIcon : StartScreenAction()
  
  object ShowKeyboard : StartScreenAction()
  
  object HideFingerprintIcon : StartScreenAction()
  
  object ShowBiometricsError : StartScreenAction()
  
  object ShowLoadingCheckingPassword : StartScreenAction()
  
  object ShowSuccessCheckingPassword : StartScreenAction()
  
  object ShowFailureCheckingPassword : StartScreenAction()
}

sealed class StartScreenUserAction : StartScreenAction() {
  
  class OnEnteredPassword(val password: String) : StartScreenUserAction()
  
  object OnEditTextTyping : StartScreenUserAction()
  
  object OnFingerprintIconClicked : StartScreenUserAction()
}

sealed class StartScreenSingleEvent {
  
  object ShowTooManyAttemptsTryAgainLater : StartScreenSingleEvent()
  
  object ShowPermanentLockout : StartScreenSingleEvent()
  
  object ShowEditTextStubPassword : StartScreenSingleEvent()
}

data class StartScreenState(
  val isLoading: Boolean = false,
  val showFingerprintIcon: Boolean = false,
  val showPasswordIsIncorrect: Boolean = false,
  val showKeyboard: Boolean = false,
)