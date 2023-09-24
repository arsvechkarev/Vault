package com.arsvechkarev.vault.features.password_entry

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.password_entry.actors.CopyPasswordEntryActor
import com.arsvechkarev.vault.features.password_entry.actors.DeletePasswordEntryActor
import com.arsvechkarev.vault.features.password_entry.actors.FetchPasswordEntryActor
import com.arsvechkarev.vault.features.password_entry.actors.OpenEditPasswordScreenCommunicatorActor
import com.arsvechkarev.vault.features.password_entry.actors.PasswordEntryRouterActor
import com.arsvechkarev.vault.features.password_entry.actors.UpdatePasswordEntryActor

fun PasswordEntryStore(
  coreComponent: CoreComponent,
  passwordId: String,
): TeaStore<PasswordEntryState, PasswordEntryUiEvent, PasswordEntryNews> {
  return TeaStoreImpl(
    actors = listOf(
      FetchPasswordEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPasswordModelInteractor,
      ),
      UpdatePasswordEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPasswordModelInteractor,
      ),
      DeletePasswordEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
      ),
      OpenEditPasswordScreenCommunicatorActor(CreatingPasswordCommunication.communicator),
      CopyPasswordEntryActor(coreComponent.clipboard),
      PasswordEntryRouterActor(coreComponent.router),
    ),
    reducer = PasswordEntryScreenReducer(),
    initialState = PasswordEntryState(passwordId)
  )
}
