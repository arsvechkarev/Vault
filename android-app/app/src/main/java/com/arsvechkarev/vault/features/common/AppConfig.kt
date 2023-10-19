package com.arsvechkarev.vault.features.common

import java.util.concurrent.TimeUnit

sealed interface AppConfig {
  
  val MaxSuccessiveBiometricsEnters: Int
  val MaxTimeSinceLastPasswordEnter: Long
  
  val MaxBackupFileCount: Int
  val DatabaseChangesForBackupThreshold: Int
  val TimePassedSinceLastBackupThreshold: Long
  
  object Debug : AppConfig {
    override val MaxSuccessiveBiometricsEnters = 5
    override val MaxTimeSinceLastPasswordEnter = TimeUnit.MINUTES.toMillis(3)
    override val MaxBackupFileCount = 5
    override val DatabaseChangesForBackupThreshold = 5
    override val TimePassedSinceLastBackupThreshold = TimeUnit.MINUTES.toMillis(1)
  }
  
  object Release : AppConfig {
    override val MaxSuccessiveBiometricsEnters = 15
    override val MaxTimeSinceLastPasswordEnter = TimeUnit.DAYS.toMillis(7)
    override val MaxBackupFileCount = 100
    override val DatabaseChangesForBackupThreshold = 5
    override val TimePassedSinceLastBackupThreshold = TimeUnit.DAYS.toMillis(3)
  }
}
