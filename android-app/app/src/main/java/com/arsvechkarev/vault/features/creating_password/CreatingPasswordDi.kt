package com.arsvechkarev.vault.features.creating_password

import com.arsvechkarev.vault.core.communicators.Communicator
import com.arsvechkarev.vault.core.communicators.CommunicatorImpl
import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.creating_password.actors.CheckPasswordStrengthActor
import com.arsvechkarev.vault.features.creating_password.actors.ComputePasswordCharacteristicsActor
import com.arsvechkarev.vault.features.creating_password.actors.CreatingPasswordRouterActor
import com.arsvechkarev.vault.features.creating_password.actors.GeneratePasswordActor
import com.arsvechkarev.vault.features.creating_password.actors.SendingOutputCreatingPasswordActor

fun CreatingPasswordStore(
  coreComponent: CoreComponent
): TeaStore<CreatingPasswordState, CreatingPasswordUiEvent, CreatingPasswordNews> {
  val communicator = CreatingPasswordCommunication.communicator
  return TeaStoreImpl(
    actors = listOf(
      GeneratePasswordActor(coreComponent.passwordGenerator),
      CheckPasswordStrengthActor(coreComponent.passwordChecker),
      SendingOutputCreatingPasswordActor(communicator),
      ComputePasswordCharacteristicsActor(coreComponent.passwordCharacteristicsProvider),
      CreatingPasswordRouterActor(coreComponent.router)
    ),
    reducer = CreatingPasswordReducer(),
    initialState = CreatingPasswordState()
  )
}

object CreatingPasswordCommunication {
  val communicator: Communicator<CreatingPasswordReceiveEvent,
      CreatingPasswordSendEvent> = CommunicatorImpl()
}
