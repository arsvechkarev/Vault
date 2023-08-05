package com.arsvechkarev.vault.features.password_info

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.common.model.PasswordItem
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.password_info.actors.CopyPasswordInfoActor
import com.arsvechkarev.vault.features.password_info.actors.DeletePasswordInfoActor
import com.arsvechkarev.vault.features.password_info.actors.OpenEditPasswordScreenActor
import com.arsvechkarev.vault.features.password_info.actors.PasswordInfoRouterActor
import com.arsvechkarev.vault.features.password_info.actors.ReceivingPasswordCommunicationActor
import com.arsvechkarev.vault.features.password_info.actors.UpdatePasswordInfoActor

fun PasswordInfoScreenStore(
  coreComponent: CoreComponent,
  passwordItem: PasswordItem,
): TeaStore<PasswordInfoState, PasswordInfoScreenUiEvent, PasswordInfoScreenNews> {
  val communicatorHolder = CreatingPasswordCommunication.communicatorHolder
  communicatorHolder.startNewCommunication()
  return TeaStoreImpl(
    actors = listOf(
      UpdatePasswordInfoActor(
        coreComponent.listenableCachedEntriesStorage,
        coreComponent.masterPasswordProvider
      ),
      DeletePasswordInfoActor(
        coreComponent.listenableCachedEntriesStorage,
        coreComponent.masterPasswordProvider
      ),
      OpenEditPasswordScreenActor(communicatorHolder),
      CopyPasswordInfoActor(coreComponent.clipboard),
      ReceivingPasswordCommunicationActor(communicatorHolder.communicator),
      PasswordInfoRouterActor(coreComponent.router),
    ),
    reducer = PasswordInfoScreenReducer(),
    initialState = PasswordInfoState(passwordItem)
  )
}
