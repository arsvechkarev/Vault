package com.arsvechkarev.vault.features.change_master_password

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.change_master_password.actors.ChangeMasterPasswordActor
import com.arsvechkarev.vault.features.change_master_password.actors.ChangeMasterPasswordRouterActor
import com.arsvechkarev.vault.features.change_master_password.actors.ValidatePasswordActor
import com.arsvechkarev.vault.features.common.di.CoreComponent

fun ChangeMasterPasswordStore(
  coreComponent: CoreComponent,
): TeaStore<ChangeMasterPasswordState, ChangeMasterPasswordUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ChangeMasterPasswordActor(
        coreComponent.masterPasswordChecker,
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
      ),
      ValidatePasswordActor(coreComponent.masterPasswordProvider),
      ChangeMasterPasswordRouterActor(coreComponent.router),
    ),
    reducer = ChangeMasterPasswordReducer(),
    initialState = ChangeMasterPasswordState()
  )
}
