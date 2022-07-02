package com.arsvechkarev.vault.features.creating_master_password

import com.arsvechkarev.vault.core.di.AppComponent
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl

fun CreatingMasterPasswordStore(
  appComponent: AppComponent
): TeaStore<CMPState, CMPUiEvent, CMPNews> {
  return TeaStoreImpl(
    actors = listOf(PasswordCheckingActor(appComponent.passwordChecker)),
    reducer = CreatingMasterPasswordReducer(appComponent.router),
    initialState = CMPState()
  )
}
