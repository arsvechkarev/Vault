package com.arsvechkarev.vault.features.master_password

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.master_password.actors.ChangeExistingMasterPasswordActor
import com.arsvechkarev.vault.features.master_password.actors.CheckPasswordStatusActor
import com.arsvechkarev.vault.features.master_password.actors.CheckPasswordStrengthActor
import com.arsvechkarev.vault.features.master_password.actors.CreateNewMasterPasswordActor
import com.arsvechkarev.vault.features.master_password.actors.CreatingMasterPasswordRouterActor

fun MasterPasswordStore(
  coreComponent: CoreComponent,
  mode: MasterPasswordScreenMode
): TeaStore<MasterPasswordState, MasterPasswordUiEvent, MasterPasswordNews> {
  return TeaStoreImpl(
    actors = listOf(
      CheckPasswordStatusActor(
        coreComponent.masterPasswordProvider,
        coreComponent.passwordChecker
      ),
      CheckPasswordStrengthActor(coreComponent.passwordChecker),
      ChangeExistingMasterPasswordActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.globalChangeMasterPasswordPublisher
      ),
      CreateNewMasterPasswordActor(coreComponent.databaseInitializer),
      CreatingMasterPasswordRouterActor(coreComponent.router),
    ),
    reducer = MasterPasswordReducer(),
    initialState = MasterPasswordState(mode)
  )
}
