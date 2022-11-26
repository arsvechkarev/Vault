package com.arsvechkarev.vault.features.change_master_password

import com.arsvechkarev.vault.core.di.AppComponent
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.change_master_password.actors.ChangeMasterPasswordActor
import com.arsvechkarev.vault.features.change_master_password.actors.ValidatePasswordActor

fun ChangeMasterPasswordStore(
  appComponent: AppComponent
): TeaStore<ChangeMasterPasswordState, ChangeMasterPasswordUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(
      ChangeMasterPasswordActor(
        appComponent.masterPasswordChecker,
        appComponent.masterPasswordProvider,
        appComponent.listenableCachedPasswordStorage,
      ),
      ValidatePasswordActor(appComponent.masterPasswordProvider)
    ),
    reducer = ChangeMasterPasswordReducer(appComponent.router),
    initialState = ChangeMasterPasswordState()
  )
}
