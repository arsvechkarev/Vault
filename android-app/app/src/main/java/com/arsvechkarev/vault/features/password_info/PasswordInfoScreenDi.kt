package com.arsvechkarev.vault.features.password_info

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.password_info.actors.CopyPasswordInfoActor
import com.arsvechkarev.vault.features.password_info.actors.DeletePasswordInfoActor
import com.arsvechkarev.vault.features.password_info.actors.FetchPasswordEntryActor
import com.arsvechkarev.vault.features.password_info.actors.OpenEditPasswordScreenActor
import com.arsvechkarev.vault.features.password_info.actors.PasswordInfoRouterActor
import com.arsvechkarev.vault.features.password_info.actors.UpdatePasswordInfoActor

fun PasswordInfoScreenStore(
  coreComponent: CoreComponent,
  passwordId: String,
): TeaStore<PasswordInfoState, PasswordInfoScreenUiEvent, PasswordInfoScreenNews> {
  return TeaStoreImpl(
    actors = listOf(
      FetchPasswordEntryActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPasswordModelInteractor,
      ),
      UpdatePasswordInfoActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
        coreComponent.keePassPasswordModelInteractor,
      ),
      DeletePasswordInfoActor(
        coreComponent.masterPasswordProvider,
        coreComponent.observableCachedDatabaseStorage,
      ),
      OpenEditPasswordScreenActor(CreatingPasswordCommunication.communicator),
      CopyPasswordInfoActor(coreComponent.clipboard),
      PasswordInfoRouterActor(coreComponent.router),
    ),
    reducer = PasswordInfoScreenReducer(),
    initialState = PasswordInfoState(passwordId)
  )
}
