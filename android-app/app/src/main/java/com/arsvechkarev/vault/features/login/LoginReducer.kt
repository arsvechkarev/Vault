package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.Error
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.CANCELLED
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.LOCKOUT
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.LOCKOUT_PERMANENT
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.ErrorType.OTHER
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEvent.Success
import com.arsvechkarev.vault.features.login.LoginCommand.EnterWithBiometrics
import com.arsvechkarev.vault.features.login.LoginCommand.EnterWithMasterPassword
import com.arsvechkarev.vault.features.login.LoginCommand.GetBiometricsEnterPossible
import com.arsvechkarev.vault.features.login.LoginCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.login.LoginEvent.BiometricsEnterNotPossible
import com.arsvechkarev.vault.features.login.LoginEvent.BiometricsEnterPossible
import com.arsvechkarev.vault.features.login.LoginEvent.ShowFailureCheckingBiometrics
import com.arsvechkarev.vault.features.login.LoginEvent.ShowFailureCheckingPassword
import com.arsvechkarev.vault.features.login.LoginEvent.ShowLoginSuccess
import com.arsvechkarev.vault.features.login.LoginNews.ShowBiometricsPrompt
import com.arsvechkarev.vault.features.login.LoginNews.ShowKeyboard
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnBiometricsEvent
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnBiometricsIconClicked
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnterWithPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnInit
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnTypingText

class LoginReducer : DslReducer<LoginState, LoginEvent, LoginCommand, LoginNews>() {
  
  override fun dslReduce(event: LoginEvent) {
    when (event) {
      OnInit -> {
        commands(GetBiometricsEnterPossible)
      }
      is BiometricsEnterPossible -> {
        state { copy(biometricsEnabled = true, biometricsIv = event.iv) }
        news(ShowBiometricsPrompt(event.iv))
      }
      BiometricsEnterNotPossible -> {
        state { copy(biometricsEnabled = false) }
        news(ShowKeyboard)
      }
      OnBiometricsIconClicked -> {
        val iv = state.biometricsIv
        if (iv != null) {
          news(ShowBiometricsPrompt(iv))
        }
      }
      is OnBiometricsEvent -> {
        when (event.event) {
          is Success -> {
            state { copy(showLoading = true, biometricsState = LoginBiometricsState.OK) }
            commands(EnterWithBiometrics(event.event.cryptography))
          }
          is Error -> {
            when (event.event.error) {
              LOCKOUT -> {
                state { copy(biometricsState = LoginBiometricsState.LOCKOUT) }
              }
              LOCKOUT_PERMANENT -> {
                state { copy(biometricsState = LoginBiometricsState.LOCKOUT_PERMANENT) }
              }
              OTHER -> {
                state { copy(biometricsState = LoginBiometricsState.OTHER_ERROR) }
              }
              CANCELLED -> Unit
            }
          }
        }
      }
      OnTypingText -> {
        state { copy(showPasswordIsIncorrect = false) }
      }
      is OnEnterWithPassword -> {
        state { copy(showLoading = true, showPasswordIsIncorrect = false) }
        commands(EnterWithMasterPassword(event.password))
      }
      ShowLoginSuccess -> {
        state { copy(showLoading = false) }
        commands(GoToMainListScreen)
      }
      ShowFailureCheckingPassword -> {
        state { copy(showLoading = false, showPasswordIsIncorrect = true) }
      }
      ShowFailureCheckingBiometrics -> {
        state { copy(showLoading = false) }
      }
    }
  }
}
