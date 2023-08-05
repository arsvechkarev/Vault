package com.arsvechkarev.vault.features.plain_text_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.data.storage.ListenableCachedEntriesStorage
import com.arsvechkarev.vault.features.common.domain.MasterPasswordProvider
import com.arsvechkarev.vault.features.common.model.toPlainText
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.UpdateItem
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent.UpdatedPlainText.UpdatedText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class UpdatePlainTextActor(
  private val masterPasswordProvider: MasterPasswordProvider,
  private val storage: ListenableCachedEntriesStorage,
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
    return commands.filterIsInstance<UpdateItem>()
        .mapLatest { command ->
          val masterPassword = masterPasswordProvider.provideMasterPassword()
          storage.updateEntry(masterPassword, command.plainTextItem.toPlainText())
          when (command) {
            is UpdateItem.UpdateTitle -> UpdatedText(command.plainTextItem)
            is UpdateItem.UpdateText -> UpdatedText(command.plainTextItem)
          }
        }
  }
}
