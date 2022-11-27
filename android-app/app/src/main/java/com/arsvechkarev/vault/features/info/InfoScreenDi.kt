package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.info.actors.CopyInfoCommandHandler
import com.arsvechkarev.vault.features.info.actors.DeletePasswordInfoActor
import com.arsvechkarev.vault.features.info.actors.InfoRouterActor
import com.arsvechkarev.vault.features.info.actors.OpenEditPasswordScreenActor
import com.arsvechkarev.vault.features.info.actors.ReceivingPasswordCommunicationActor
import com.arsvechkarev.vault.features.info.actors.UpdatePasswordInfoActor

fun InfoScreenStore(
  coreComponent: CoreComponent,
  passwordInfoItem: PasswordInfoItem,
): TeaStore<InfoScreenState, InfoScreenUiEvent, InfoScreenNews> {
  val communicatorHolder = CreatingPasswordCommunication.communicatorHolder
  // TODO (27.11.2022): Figure out a good way to start and stop communication
  communicatorHolder.startNewCommunication()
  return TeaStoreImpl(
    actors = listOf(
      UpdatePasswordInfoActor(
        coreComponent.listenableCachedPasswordsStorage,
        coreComponent.masterPasswordProvider
      ),
      DeletePasswordInfoActor(
        coreComponent.listenableCachedPasswordsStorage,
        coreComponent.masterPasswordProvider
      ),
      OpenEditPasswordScreenActor(communicatorHolder),
      CopyInfoCommandHandler(coreComponent.clipboard),
      ReceivingPasswordCommunicationActor(communicatorHolder.communicator),
      InfoRouterActor(coreComponent.router),
    ),
    reducer = InfoScreenReducer(),
    initialState = InfoScreenState(passwordInfoItem)
  )
}
