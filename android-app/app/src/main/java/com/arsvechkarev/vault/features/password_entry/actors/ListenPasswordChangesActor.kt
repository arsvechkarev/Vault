package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.PasswordObserver
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent.PasswordUpdated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ListenPasswordChangesActor(
  private val passwordObserver: PasswordObserver
) : Actor<PasswordEntryCommand, PasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordEntryCommand>): Flow<PasswordEntryEvent> {
    return passwordObserver.passwordChanges.mapLatest(::PasswordUpdated)
  }
}
