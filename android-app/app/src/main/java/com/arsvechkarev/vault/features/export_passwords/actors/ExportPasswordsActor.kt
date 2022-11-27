package com.arsvechkarev.vault.features.export_passwords.actors

import android.content.Context
import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.DispatchersFacade
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.ExportPasswords
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.ExportedPasswords
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

class ExportPasswordsActor(
  private val context: Context,
  private val fileSaver: FileSaver,
  private val dispatchersFacade: DispatchersFacade
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance<ExportPasswords>()
        .mapLatest { command ->
          withContext(dispatchersFacade.IO) {
            val outputStream = context.contentResolver.openOutputStream(command.uri, "wr")
            checkNotNull(outputStream).use {
              it.write(checkNotNull(fileSaver.readData()))
            }
          }
          ExportedPasswords
        }
  }
}
