package com.arsvechkarev.vault.features.settings

import com.arsvechkarev.vault.core.mvi.tea.TeaStore
import com.arsvechkarev.vault.core.mvi.tea.TeaStoreImpl
import com.arsvechkarev.vault.features.common.di.CoreComponent
import com.arsvechkarev.vault.features.settings.actors.ChangeShowUsernamesActor
import com.arsvechkarev.vault.features.settings.actors.ClearImagesCacheActor
import com.arsvechkarev.vault.features.settings.actors.DisableBiometricsActor
import com.arsvechkarev.vault.features.settings.actors.EnableBiometricsActor
import com.arsvechkarev.vault.features.settings.actors.GetBiometricsAvailableActor
import com.arsvechkarev.vault.features.settings.actors.GetBiometricsEnabledActor
import com.arsvechkarev.vault.features.settings.actors.GetShowUsernamesActor
import com.arsvechkarev.vault.features.settings.actors.ObserveMasterPasswordChangesActor
import com.arsvechkarev.vault.features.settings.actors.SettingsRouterActor

fun SettingsStore(
  coreComponent: CoreComponent,
): TeaStore<SettingsState, SettingsUiEvent, SettingsNews> {
  return TeaStoreImpl(
    actors = listOf(
      GetShowUsernamesActor(coreComponent.showUsernamesInteractor),
      GetBiometricsAvailableActor(coreComponent.biometricsAvailabilityProvider),
      GetBiometricsEnabledActor(coreComponent.biometricsEnabledProvider),
      EnableBiometricsActor(coreComponent.masterPasswordProvider, coreComponent.biometricsStorage),
      DisableBiometricsActor(coreComponent.biometricsStorage),
      ChangeShowUsernamesActor(coreComponent.showUsernamesInteractor),
      ObserveMasterPasswordChangesActor(coreComponent.changeMasterPasswordObserver),
      ClearImagesCacheActor(
        coreComponent.imagesCache,
        coreComponent.imagesNamesLoader,
        coreComponent.reloadImagesObserver
      ),
      SettingsRouterActor(coreComponent.router),
    ),
    reducer = SettingsReducer(),
    initialState = SettingsState()
  )
}

