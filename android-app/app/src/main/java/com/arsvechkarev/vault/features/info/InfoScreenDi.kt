package com.arsvechkarev.vault.features.info

import com.arsvechkarev.vault.core.model.PasswordInfoItem
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.AppComponent
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordCommunication
import com.arsvechkarev.vault.features.info.actors.CopyInfoCommandHandler
import com.arsvechkarev.vault.features.info.actors.DeletePasswordInfoActor
import com.arsvechkarev.vault.features.info.actors.OpenEditPasswordScreenActor
import com.arsvechkarev.vault.features.info.actors.ReceivingPasswordCommunicationActor
import com.arsvechkarev.vault.features.info.actors.UpdatePasswordInfoActor

fun InfoScreenStore(
  appComponent: AppComponent,
  passwordInfoItem: PasswordInfoItem,
): TeaStore<InfoScreenState, InfoScreenUiEvent, InfoScreenNews> {
  val communicatorHolder = CreatingPasswordCommunication.communicatorHolder
  communicatorHolder.startNewCommunication()
  return TeaStoreImpl(
    actors = listOf(
      UpdatePasswordInfoActor(
        appComponent.listenableCachedPasswordsStorage,
        appComponent.masterPasswordProvider
      ),
      DeletePasswordInfoActor(
        appComponent.listenableCachedPasswordsStorage,
        appComponent.masterPasswordProvider
      ),
      OpenEditPasswordScreenActor(communicatorHolder),
      CopyInfoCommandHandler(appComponent.clipboard),
      ReceivingPasswordCommunicationActor(communicatorHolder.communicator),
    ),
    reducer = InfoScreenReducer(appComponent.router),
    initialState = InfoScreenState(passwordInfoItem)
  )
}
