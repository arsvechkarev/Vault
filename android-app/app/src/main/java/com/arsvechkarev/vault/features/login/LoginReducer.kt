package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
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
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnterWithBiometrics
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
        news(ShowBiometricsPrompt(event.iv))
      }
      BiometricsEnterNotPossible -> {
        // TODO (10/15/23): Show on UI that biometrics is not possible
      }
      is OnEnterWithBiometrics -> {
        state { copy(showLoading = true) }
        commands(EnterWithBiometrics(event.cryptography))
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
