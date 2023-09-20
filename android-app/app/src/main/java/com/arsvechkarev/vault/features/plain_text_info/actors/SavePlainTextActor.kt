package com.arsvechkarev.vault.features.plain_text_info.actors

import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import buisnesslogic.model.PlainTextEntry
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.SavePlainText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.NotifyEntryCreated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class SavePlainTextActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val plainTextModelInteractor: KeePassPlainTextModelInteractor,
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
    return commands.filterIsInstance<SavePlainText>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val databaseUUIDPair = plainTextModelInteractor.addPlainText(database, command.data)
          val createdEntry = PlainTextEntry(
            id = databaseUUIDPair.second,
            title = command.data.title,
            text = command.data.text
          )
          NotifyEntryCreated(createdEntry)
        }
  }
}
