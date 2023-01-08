package com.arsvechkarev.vault.features.creating_master_password

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_master_password.actors.CreatingMasterPasswordRouterActor
import com.arsvechkarev.vault.features.creating_master_password.actors.FinishAuthActor
import com.arsvechkarev.vault.features.creating_master_password.actors.PasswordCheckingActor

fun CreatingMasterPasswordStore(
  coreComponent: CoreComponent
): TeaStore<CMPState, CMPUiEvent, CMPNews> {
  return TeaStoreImpl(
    actors = listOf(
      PasswordCheckingActor(coreComponent.passwordInfoChecker),
      FinishAuthActor(
        coreComponent.masterPasswordChecker,
        coreComponent.dispatchersFacade
      ),
      CreatingMasterPasswordRouterActor(coreComponent.router),
    ),
    reducer = CreatingMasterPasswordReducer(coreComponent.router),
    initialState = CMPState()
  )
}
