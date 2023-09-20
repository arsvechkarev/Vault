package com.arsvechkarev.vault.features.plain_text_info.actors

import app.keemobile.kotpass.database.modifiers.removeEntry
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.DeletePlainText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.NotifyEntryDeleted
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest
import java.util.UUID

class DeletePlainTextActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
    return commands.filterIsInstance<DeletePlainText>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = database.removeEntry(UUID.fromString(command.plainTextId))
          storage.saveDatabase(newDatabase)
          NotifyEntryDeleted
        }
  }
}
