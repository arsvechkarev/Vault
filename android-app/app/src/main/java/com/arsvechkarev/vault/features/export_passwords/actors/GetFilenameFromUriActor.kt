package com.arsvechkarev.vault.features.export_passwords.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.FilenameFromUriRetriever
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsCommand.CalculateFilenameFromUri
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsEvent.CalculatedFilenameFromUri
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class GetFilenameFromUriActor(
  private val filenameFromUriRetriever: FilenameFromUriRetriever
) : Actor<ExportPasswordsCommand, ExportPasswordsEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<ExportPasswordsCommand>): Flow<ExportPasswordsEvent> {
    return commands.filterIsInstance<CalculateFilenameFromUri>()
        .mapLatest { command ->
          val filename = filenameFromUriRetriever.getFilename(command.uri, command.fallback)
          CalculatedFilenameFromUri(filename)
        }
  }
}
