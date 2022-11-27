package com.arsvechkarev.vault.features.change_master_password

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.change_master_password.actors.ChangeMasterPasswordActor
import com.arsvechkarev.vault.features.change_master_password.actors.ValidatePasswordActor
import com.arsvechkarev.vault.features.common.di.AppComponent

fun ChangeMasterPasswordStore(
  appComponent: AppComponent
): TeaStore<ChangeMasterPasswordState, ChangeMasterPasswordUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ChangeMasterPasswordActor(
        appComponent.masterPasswordChecker,
        appComponent.masterPasswordProvider,
        appComponent.listenableCachedPasswordsStorage,
      ),
      ValidatePasswordActor(appComponent.masterPasswordProvider)
    ),
    reducer = ChangeMasterPasswordReducer(appComponent.router),
    initialState = ChangeMasterPasswordState()
  )
}
