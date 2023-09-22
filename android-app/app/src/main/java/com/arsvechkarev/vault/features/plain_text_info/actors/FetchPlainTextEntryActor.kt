package com.arsvechkarev.vault.features.plain_text_info.actors

import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.FetchPlainTextEntry
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.ReceivedPlainTextEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class FetchPlainTextEntryActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val plainTextModelInteractor: KeePassPlainTextModelInteractor,
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
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