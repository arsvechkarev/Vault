package com.arsvechkarev.vault.features.plain_text_entry.actors

import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryCommand.FetchPlainTextEntry
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEntryEvent.ReceivedPlainTextEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class FetchPlainTextEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val plainTextModelInteractor: KeePassPlainTextModelInteractor,
) : Actor<PlainTextEntryCommand, PlainTextEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextEntryCommand>): Flow<PlainTextEntryEvent> {
    return commands.filterIsInstance<FetchPlainTextEntry>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val plainTextEntry = plainTextModelInteractor.getPlainTextEntry(database,
            command.plainTextId)
          ReceivedPlainTextEntry(plainTextEntry)
        }
  }
}