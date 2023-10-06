package com.arsvechkarev.vault.features.login.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.login.LoginCommand
import com.arsvechkarev.vault.features.login.LoginCommand.EnterWithMasterPassword
import com.arsvechkarev.vault.features.login.LoginEvent
import com.arsvechkarev.vault.features.login.LoginEvent.ShowFailureCheckingPassword
import com.arsvechkarev.vault.features.login.LoginEvent.ShowSuccessCheckingPassword
import domain.MasterPasswordChecker
import domain.MasterPasswordHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class LoginActor(
  private val masterPasswordChecker: MasterPasswordChecker
) : Actor<LoginCommand, LoginEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<LoginCommand>): Flow<LoginEvent> {
    return commands.filterIsInstance<EnterWithMasterPassword>()
        .mapLatest { command ->
          if (masterPasswordChecker.isCorrect(command.password)) {
            MasterPasswordHolder.setMasterPassword(command.password)
            ShowSuccessCheckingPassword
          } else {
            ShowFailureCheckingPassword
          }
        }
  }
}
