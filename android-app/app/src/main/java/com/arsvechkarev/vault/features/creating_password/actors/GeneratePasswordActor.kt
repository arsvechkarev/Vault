package com.arsvechkarev.vault.features.creating_password.actors

import buisnesslogic.generator.PasswordGenerator
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommand.GeneratePassword
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordEvent.GeneratedPassword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class GeneratePasswordActor(
  private val passwordGenerator: PasswordGenerator
) : Actor<CreatingPasswordCommand, CreatingPasswordEvent> {
  
  override fun handle(commands: Flow<CreatingPasswordCommand>): Flow<CreatingPasswordEvent> {
    return commands.filterIsInstance<GeneratePassword>()
        .mapLatest {
          val password = passwordGenerator.generatePassword(it.length, it.characteristics)
          GeneratedPassword(password)
        }
  }
}
