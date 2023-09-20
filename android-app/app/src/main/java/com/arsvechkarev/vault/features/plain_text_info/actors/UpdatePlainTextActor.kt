package com.arsvechkarev.vault.features.plain_text_info.actors

import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_info.PlainTextCommand.UpdateItem
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.UpdatedPlainText.UpdatedText
import com.arsvechkarev.vault.features.plain_text_info.PlainTextEvent.UpdatedPlainText.UpdatedTitle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePlainTextActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ObservableCachedDatabaseStorage,
  private val plainTextModelInteractor: KeePassPlainTextModelInteractor
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
    return commands.filterIsInstance<UpdateItem>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          val database = storage.getDatabase(masterPassword)
          val newDatabase = plainTextModelInteractor.editPlainText(database, command.plainTextEntry)
          storage.saveDatabase(newDatabase)
          when (command) {
            is UpdateItem.UpdateTitle -> UpdatedTitle(command.plainTextEntry)
            is UpdateItem.UpdateText -> UpdatedText(command.plainTextEntry)
          }
        }
  }
}
