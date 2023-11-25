package com.arsvechkarev.vault.features.note_entry.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.Router
import com.arsvechkarev.vault.features.common.Screens
import com.arsvechkarev.vault.features.common.navigation.RouterActor
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.RouterCommand
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.RouterCommand.GoBack
import com.arsvechkarev.vault.features.note_entry.NoteEntryCommand.RouterCommand.SwitchBackToLogin
import com.arsvechkarev.vault.features.note_entry.NoteEntryEvent

fun NoteRouterActor(
  router: Router
): Actor<NoteEntryCommand, NoteEntryEvent> {
  return RouterActor<NoteEntryCommand, RouterCommand, NoteEntryEvent>(router) { command ->
    when (command) {
      GoBack -> goBack()
      SwitchBackToLogin -> switchToNewRoot(Screens.LoginScreen)
    }
  }
}