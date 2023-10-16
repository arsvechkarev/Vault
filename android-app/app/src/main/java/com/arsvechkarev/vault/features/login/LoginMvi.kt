package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.features.common.biometrics.BiometricsCryptography
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent
import domain.Password

sealed interface LoginEvent {
  object ShowLoginSuccess : LoginEvent
  object ShowFailureCheckingPassword : LoginEvent
  object ShowFailureCheckingBiometrics : LoginEvent
  class BiometricsEnterPossible(val iv: ByteArray) : LoginEvent
  object BiometricsEnterNotEnabled : LoginEvent
  object BiometricsEnterNotAllowed : LoginEvent
}

sealed interface LoginUiEvent : LoginEvent {
  object OnInit : LoginUiEvent
  class OnEnterWithPassword(val password: Password) : LoginUiEvent
  object OnBiometricsIconClicked : LoginUiEvent
  class OnBiometricsEvent(val event: BiometricsEvent) : LoginUiEvent
  object OnTypingText : LoginUiEvent
}

sealed interface LoginCommand {
  object GetBiometricsEnterPossible : LoginCommand
  class EnterWithMasterPassword(val password: Password) : LoginCommand
  class EnterWithBiometrics(val cryptography: BiometricsCryptography) : LoginCommand
  object GoToMainListScreen : LoginCommand
}

sealed interface LoginNews {
  object ShowKeyboard : LoginNews
  class ShowBiometricsPrompt(val iv: ByteArray) : LoginNews
}

data class LoginState(
  val showLoading: Boolean = false,
  val biometricsEnabled: Boolean = false,
  val biometricsIv: ByteArray? = null,
  val biometricsState: LoginBiometricsState = LoginBiometricsState.OK,
  val showPasswordIsIncorrect: Boolean = false,
) {
  
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    
    other as LoginState
    
    if (showLoading != other.showLoading) return false
    if (biometricsEnabled != other.biometricsEnabled) return false
    if (biometricsIv != null) {
      if (other.biometricsIv == null) return false
      if (!biometricsIv.contentEquals(other.biometricsIv)) return false
    } else if (other.biometricsIv != null) return false
    if (biometricsState != other.biometricsState) return false
    return showPasswordIsIncorrect == other.showPasswordIsIncorrect
  }
  
  override fun hashCode(): Int {
    var result = showLoading.hashCode()
    result = 31 * result + biometricsEnabled.hashCode()
    result = 31 * result + (biometricsIv?.contentHashCode() ?: 0)
    result = 31 * result + biometricsState.hashCode()
    result = 31 * result + showPasswordIsIncorrect.hashCode()
    return result
  }
}

enum class LoginBiometricsState {
  OK, NOT_ALLOWED, LOCKOUT, LOCKOUT_PERMANENT, OTHER_ERROR
}
