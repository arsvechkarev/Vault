package com.arsvechkarev.vault.features.login.actors

import com.arsvechkarev.vault.core.mvi.tea.Actor
import com.arsvechkarev.vault.features.common.biometrics.BiometricsEnabledProvider
import com.arsvechkarev.vault.features.common.biometrics.BiometricsStorage
import com.arsvechkarev.vault.features.login.LoginCommand
import com.arsvechkarev.vault.features.login.LoginCommand.GetBiometricsEnterPossible
import com.arsvechkarev.vault.features.login.LoginEvent
import com.arsvechkarev.vault.features.login.LoginEvent.BiometricsEnterNotPossible
import com.arsvechkarev.vault.features.login.LoginEvent.BiometricsEnterPossible
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapLatest

class GetBiometricsEnterPossibleActor(
  private val biometricsEnabledProvider: BiometricsEnabledProvider,
  private val biometricsStorage: BiometricsStorage,
) : Actor<LoginCommand, LoginEvent> {
  
  @OptIn(ExperimentalCoroutinesApi::class)
  override fun handle(commands: Flow<LoginCommand>): Flow<LoginEvent> {
    return commands.filterIsInstance<GetBiometricsEnterPossible>()
        .mapLatest {
          val enabled = biometricsEnabledProvider.isBiometricsEnabled()
          // TODO (10/15/23): Make biometric enter not possible if user hasn't entered
          //  password for a while
          println("qqq: enabled biometrics = $enabled")
          return@mapLatest if (enabled) {
            val data = biometricsStorage.getBiometricsData()
            BiometricsEnterPossible(data.second)
          } else {
            BiometricsEnterNotPossible
          }
        }
  }
}