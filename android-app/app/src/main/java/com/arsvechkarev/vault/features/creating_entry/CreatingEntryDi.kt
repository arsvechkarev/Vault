package com.arsvechkarev.vault.features.creating_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_entry.actors.ReceivingPasswordCommunicationActor
import com.arsvechkarev.vault.features.creating_entry.actors.SavingEntryActor
import com.arsvechkarev.vault.features.creating_entry.actors.ValidateInputActor
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication

fun CreatingEntryStore(
  coreComponent: CoreComponent
): TeaStore<CreatingEntryState, CreatingEntryUiEvent, Nothing> {
  val communicatorHolder = CreatingPasswordCommunication.communicatorHolder
  communicatorHolder.startNewCommunication()
  return TeaStoreImpl(
    actors = listOf(
      ValidateInputActor(communicatorHolder),
      ReceivingPasswordCommunicationActor(communicatorHolder.communicator),
      SavingEntryActor(
        coreComponent.listenableCachedPasswordsStorage,
        coreComponent.masterPasswordProvider
      ),
    ),
    reducer = CreatingEntryReducer(coreComponent.router),
    initialState = CreatingEntryState()
  )
}
