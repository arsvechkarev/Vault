package com.arsvechkarev.vault.features.creating_entry.actors

import com.arsvechkarev.vault.core.Router
import com.arsvechkarev.vault.core.Screens.InfoScreen
import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryCommand.GoToInfoScreen
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest

class GoingToInfoScreenCommandHandler(
  private val router: Router
) : Actor<CreatingEntryCommand, CreatingEntryEvent> {
  
  override fun handle(commands: Flow<CreatingEntryCommand>): Flow<CreatingEntryEvent> {
    return commands.filterIsInstance<GoToInfoScreen>()
        .flatMapLatest { command ->
          router.goForwardWithRemovalOf(InfoScreen(command.passwordInfoItem), 2)
          emptyFlow()
        }
  }
}
