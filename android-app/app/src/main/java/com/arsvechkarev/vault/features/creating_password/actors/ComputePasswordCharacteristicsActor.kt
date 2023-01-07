package com.arsvechkarev.vault.features.creating_password.actors

import buisnesslogic.PasswordCharacteristicsProvider
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.ComputePasswordCharacteristics
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.ComputedPasswordCharacteristics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ComputePasswordCharacteristicsActor(
  private val passwordCharacteristicsProvider: PasswordCharacteristicsProvider
) : Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  
  override fun handle(commands: Flow<CreatingPasswordCommand>): Flow<CreatingPasswordEvent> {
    return commands.filterIsInstance<ComputePasswordCharacteristics>()
        .mapLatest { command ->
          ComputedPasswordCharacteristics(
            passwordCharacteristicsProvider.getCharacteristics(command.password)
          )
        }
  }
}
