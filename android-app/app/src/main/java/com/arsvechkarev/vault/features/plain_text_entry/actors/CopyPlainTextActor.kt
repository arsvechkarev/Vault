package com.arsvechkarev.vault.features.plain_text_entry.actors

import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextCommand.Copy
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class CopyPlainTextActor(
  private val clipboard: Clipboard,
) : Actor<PlainTextCommand, PlainTextEvent> {
  
  override fun handle(commands: Flow<PlainTextCommand>): Flow<PlainTextEvent> {
    return commands.filterIsInstance<Copy>()
        .emptyMap { clipboard.copyToClipboard(it.labelRes, it.text) }
  }
}
