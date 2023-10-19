package com.arsvechkarev.vault.features.settings.actors

import android.net.Uri
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.settings.SettingsCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.FetchData
import com.arsvechkarev.vault.features.settings.SettingsCommand.FetchStorageBackupEnabled
import com.arsvechkarev.vault.features.settings.SettingsEvent
import com.arsvechkarev.vault.features.settings.SettingsEvent.ReceivedStorageBackupEnabled
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest

class GetStorageBackupEnabledActor(
  private val preferences: StorageBackupPreferences
) : Actor<SettingsCommand, SettingsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
    return commands.filter { it is FetchData || it is FetchStorageBackupEnabled }
        .mapLatest {
          val (enabled, backupFolder) = preferences.getStorageBackupEnabledPair()
          ReceivedStorageBackupEnabled(enabled, backupFolder?.let(Uri::parse))
        }
  }
}