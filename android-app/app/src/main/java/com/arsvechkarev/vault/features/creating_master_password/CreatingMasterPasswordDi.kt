package com.arsvechkarev.vault.features.creating_master_password

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.creating_master_password.actors.FinishAuthActor
import com.arsvechkarev.vault.features.creating_master_password.actors.PasswordCheckingActor

fun CreatingMasterPasswordStore(
  appComponent: AppComponent
): TeaStore<CMPState, CMPUiEvent, CMPNews> {
  return TeaStoreImpl(
    actors = listOf(
      PasswordCheckingActor(appComponent.passwordInfoChecker),
      FinishAuthActor(appComponent.authChecker, appComponent.masterPasswordChecker,
        appComponent.dispatchersFacade)
    ),
    reducer = CreatingMasterPasswordReducer(appComponent.router),
    initialState = CMPState()
  )
}
