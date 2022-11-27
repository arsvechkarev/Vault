package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.communicators.CommunicatorHolder
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password.actors.CheckPasswordStrengthActor
import com.arsvechkarev.vault.features.creating_password.actors.CreatingPasswordRouterActor
import com.arsvechkarev.vault.features.creating_password.actors.GeneratePasswordActor
import com.arsvechkarev.vault.features.creating_password.actors.ReceivingInputCreatingPasswordActor
import com.arsvechkarev.vault.features.creating_password.actors.SendingOutputCreatingPasswordActor

fun CreatingPasswordStore(
  coreComponent: CoreComponent
): TeaStore<CreatingPasswordState, CreatingPasswordUiEvent, CreatingPasswordNews> {
  val communicator = CreatingPasswordCommunication.communicatorHolder.communicator
  return TeaStoreImpl(
    actors = listOf(
      GeneratePasswordActor(coreComponent.passwordGenerator),
      CheckPasswordStrengthActor(coreComponent.passwordInfoChecker),
      ReceivingInputCreatingPasswordActor(communicator),
      SendingOutputCreatingPasswordActor(communicator),
      CreatingPasswordRouterActor(coreComponent.router)
    ),
    reducer = CreatingPasswordReducer(),
    initialState = CreatingPasswordState()
  )
}

object CreatingPasswordCommunication {
  val communicatorHolder =
      CommunicatorHolder<CreatingPasswordReceiveEvent, CreatingPasswordSendEvent>()
}
