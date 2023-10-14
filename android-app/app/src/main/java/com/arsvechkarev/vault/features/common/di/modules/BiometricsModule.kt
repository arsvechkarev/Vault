package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityChecker
import com.arsvechkarev.vault.features.common.biometrics.BiometricsAvailabilityCheckerImpl

interface BiometricsModule {
  val biometricsAvailabilityChecker: BiometricsAvailabilityChecker
}

class BiometricsModuleImpl(
  coreModule: CoreModule
) : BiometricsModule {
  
  override val biometricsAvailabilityChecker =
      BiometricsAvailabilityCheckerImpl(coreModule.application)
}
