package com.arsvechkarev.vault.features.login

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.login.actors.LoginActor

fun LoginStore(
  appComponent: AppComponent
): TeaStore<LoginState, LoginUiEvent, Nothing> {
  return TeaStoreImpl(
    actors = listOf(LoginActor(appComponent.masterPasswordChecker)),
    reducer = LoginReducer(appComponent.router),
    initialState = LoginState()
  )
}
