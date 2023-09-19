package com.arsvechkarev.vault.features.export_passwords.actors

import buisnesslogic.DatabaseSaver
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.ExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.ExportedPasswords
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ExportPasswordsActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val databaseSaver: DatabaseSaver,
  private val passwordsFileWriter: PasswordsFileExporter,
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance<ExportPasswords>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = checkNotNull(databaseSaver.read(masterPassword))
          passwordsFileWriter.writeData(command.uri, database)
          ExportedPasswords
        }
  }
}
