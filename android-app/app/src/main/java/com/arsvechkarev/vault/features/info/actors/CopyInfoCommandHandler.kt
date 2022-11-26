package com.arsvechkarev.vault.features.info.actors

import com.arsvechkarev.vault.core.Clipboard
import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.info.InfoScreenCommand
import com.arsvechkarev.vault.features.info.InfoScreenCommand.Copy
import com.arsvechkarev.vault.features.info.InfoScreenEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class CopyInfoCommandHandler(
  private val clipboard: Clipboard,
) : Actor<InfoScreenCommand, InfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<InfoScreenEvent> {
    return commands.filterIsInstance<Copy>()
        .emptyMap { clipboard.copyToClipboard(it.labelRes, it.text) }
  }
}
