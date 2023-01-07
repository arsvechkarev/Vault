package com.arsvechkarev.vault.features.export_passwords.actors

import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.PasswordsFileExporter
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.ExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.ExportedPasswords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class ExportPasswordsActor(
  private val fileSaver: FileSaver,
  private val passwordsFileWriter: PasswordsFileExporter,
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance<ExportPasswords>()
        .mapLatest { command ->
          passwordsFileWriter.writeData(command.uri, checkNotNull(fileSaver.readData()))
          ExportedPasswords
        }
  }
}
