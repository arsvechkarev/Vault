package com.arsvechkarev.vault.features.export_passwords.actors

import buisnesslogic.DatabaseSaver
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.GetPasswordsFileUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.PasswordsFileUriReceived
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class GetPasswordsFileUriActor(
  private val databaseSaver: DatabaseSaver
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance<GetPasswordsFileUri>()
        .mapLatest {
          PasswordsFileUriReceived(databaseSaver.getFileUri())
        }
  }
}
