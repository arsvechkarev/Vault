package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.settings.actors.ChangeShowUsernamesActor
import com.arsvechkarev.vault.features.settings.actors.GetShowUsernamesActor
import com.arsvechkarev.vault.features.settings.actors.ObserveMasterPasswordChangesActor
import com.arsvechkarev.vault.features.settings.actors.SettingsRouterActor

fun SettingsStore(
  coreComponent: CoreComponent,
): TeaStore<SettingsState, SettingsUiEvent, SettingsNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetShowUsernamesActor(coreComponent.showUsernamesInteractor),
      ChangeShowUsernamesActor(coreComponent.showUsernamesInteractor),
      ObserveMasterPasswordChangesActor(coreComponent.globalChangeMasterPasswordSubscriber),
      SettingsRouterActor(coreComponent.router),
    ),
    reducer = SettingsReducer(),
    initialState = SettingsState()
  )
}

