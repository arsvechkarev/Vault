package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.DslReducer
import com.arsvechkarev.vault.features.login.LoginCommand.EnterWithMasterPassword
import com.arsvechkarev.vault.features.login.LoginCommand.GoToMainListScreen
import com.arsvechkarev.vault.features.login.LoginEvent.ShowFailureCheckingPassword
import com.arsvechkarev.vault.features.login.LoginEvent.ShowSuccessCheckingPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnAppearedOnScreen
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnEnteredPassword
import com.arsvechkarev.vault.features.login.LoginUiEvent.OnTypingText

class LoginReducer : DslReducer<LoginState, LoginEvent, LoginCommand, Nothing>() {
  
  override fun dslReduce(event: LoginEvent) {
    when (event) {
      OnAppearedOnScreen -> {
        state { copy(showKeyboard = true) }
      }
      OnTypingText -> {
        state { copy(showPasswordIsIncorrect = false) }
      }
      is OnEnteredPassword -> {
        state { copy(isLoading = true, showKeyboard = true, showPasswordIsIncorrect = false) }
        commands(EnterWithMasterPassword(event.password))
      }
      ShowFailureCheckingPassword -> {
        state { copy(isLoading = false, showPasswordIsIncorrect = true) }
      }
      ShowSuccessCheckingPassword -> {
        state { copy(isLoading = false, showKeyboard = false) }
        commands(GoToMainListScreen)
      }
    }
  }
}
