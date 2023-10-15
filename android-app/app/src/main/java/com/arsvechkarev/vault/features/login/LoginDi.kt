package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.login.actors.GetBiometricsEnterPossibleActor
import com.arsvechkarev.vault.features.login.actors.LoginRouterActor
import com.arsvechkarev.vault.features.login.actors.LoginWithBiometricsActor
import com.arsvechkarev.vault.features.login.actors.LoginWithPasswordActor

fun LoginStore(
  coreComponent: CoreComponent
): TeaStore<LoginState, LoginUiEvent, LoginNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetBiometricsEnterPossibleActor(
        coreComponent.biometricsEnabledProvider,
        coreComponent.biometricsStorage
      ),
      LoginWithBiometricsActor(
        coreComponent.biometricsStorage,
        coreComponent.masterPasswordChecker
      ),
      LoginWithPasswordActor(coreComponent.masterPasswordChecker),
      LoginRouterActor(coreComponent.router)
    ),
    reducer = LoginReducer(),
    initialState = LoginState()
  )
}
