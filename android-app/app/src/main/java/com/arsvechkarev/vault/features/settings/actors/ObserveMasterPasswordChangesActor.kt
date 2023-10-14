package com.arsvechkarev.vault.features.settings.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.ChangeMasterPasswordObserver
import com.arsvechkarev.vault.features.settings.SettingsCommand
import com.arsvechkarev.vault.features.settings.SettingsEvent
import com.arsvechkarev.vault.features.settings.SettingsEvent.MasterPasswordChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class ObserveMasterPasswordChangesActor(
  private val changeMasterPasswordObserver: ChangeMasterPasswordObserver
) : Actor<SettingsCommand, SettingsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
    return changeMasterPasswordObserver.masterPasswordChanges
        .mapLatest { MasterPasswordChanged }
  }
}