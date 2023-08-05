package com.arsvechkarev.vault.features.creating_password.actors

import buisnesslogic.PasswordInfoChecker
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.CheckPasswordStrength
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.PasswordStrengthChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class CheckPasswordStrengthActor(
  private val passwordInfoChecker: PasswordInfoChecker
) : Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<CreatingPasswordCommand>): Flow<CreatingPasswordEvent> {
    return commands.filterIsInstance<CheckPasswordStrength>()
        .mapLatest {
          PasswordStrengthChanged(passwordInfoChecker.checkStrength(it.password))
        }
  }
}
