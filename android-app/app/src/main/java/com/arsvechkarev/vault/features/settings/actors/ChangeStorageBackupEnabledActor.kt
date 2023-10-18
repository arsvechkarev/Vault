package com.arsvechkarev.vault.features.settings.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.settings.SettingsCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand.DisableStorageBackup
import com.arsvechkarev.vault.features.settings.SettingsCommand.BackupCommand.EnableStorageBackup
import com.arsvechkarev.vault.features.settings.SettingsEvent
import com.arsvechkarev.vault.features.settings.SettingsEvent.StorageBackupDisabled
import com.arsvechkarev.vault.features.settings.SettingsEvent.StorageBackupEnabled
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ChangeStorageBackupEnabledActor(
  private val preferences: StorageBackupPreferences
) : Actor<SettingsCommand, SettingsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<SettingsCommand>): Flow<SettingsEvent> {
    return commands.filterIsInstance<BackupCommand>()
        .mapLatest { command ->
          when (command) {
            is EnableStorageBackup -> {
              preferences.enableBackup(command.backupFolderUri.toString())
              StorageBackupEnabled(command.backupFolderUri)
            }
            DisableStorageBackup -> {
              preferences.disableBackup()
              StorageBackupDisabled
            }
          }
        }
  }
}
