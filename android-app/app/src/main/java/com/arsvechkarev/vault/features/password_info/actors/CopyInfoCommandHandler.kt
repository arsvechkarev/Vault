package com.arsvechkarev.vault.features.password_info.actors

import com.arsvechkarev.vault.core.extensions.emptyMap
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.domain.Clipboard
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand
import com.arsvechkarev.vault.features.password_info.InfoScreenCommand.Copy
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreenEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance

class CopyInfoCommandHandler(
  private val clipboard: Clipboard,
) : Actor<InfoScreenCommand, PasswordInfoScreenEvent> {
  
  override fun handle(commands: Flow<InfoScreenCommand>): Flow<PasswordInfoScreenEvent> {
    return commands.filterIsInstance<Copy>()
        .emptyMap { clipboard.copyToClipboard(it.labelRes, it.text) }
  }
}
