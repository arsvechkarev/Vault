package com.arsvechkarev.vault.features.password_entry.actors

import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand
import com.arsvechkarev.vault.features.password_entry.PasswordEntryCommand.Copy
import com.arsvechkarev.vault.features.password_entry.PasswordEntryEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class CopyPasswordEntryActor(
  private val clipboard: Clipboard,
) : Actor<PasswordEntryCommand, PasswordEntryEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<PasswordEntryCommand>): Flow<PasswordEntryEvent> {
    return commands.filterIsInstance<Copy>()
        .emptyMap { clipboard.copyToClipboard(it.labelRes, it.text) }
  }
}
