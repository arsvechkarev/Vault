package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.features.common.AppConfig
import com.arsvechkarev.vault.features.common.data.StorageBackupFileSaver
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournal
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournalImpl
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor
import com.arsvechkarev.vault.features.common.domain.StorageBackupInteractor

interface DomainModule {
  val databaseChangesJournal: DatabaseChangesJournal
  val storageBackupPreferences: StorageBackupPreferences
  val storageBackupInteractor: StorageBackupInteractor
  val showUsernamesInteractor: ShowUsernamesInteractor
}

class DomainModuleImpl(
  coreModule: CoreModule,
  preferencesModule: PreferencesModule
) : DomainModule {
  
  override val databaseChangesJournal = DatabaseChangesJournalImpl(
    preferencesModule.settingsPreferences
  )
  
  override val storageBackupPreferences = StorageBackupPreferences(
    preferencesModule.settingsPreferences
  )
  
  private val passedTimeSinceLastBackupThreshold = if (BuildConfig.DEBUG) {
    AppConfig.Debug.TimePassedSinceLastBackupThreshold
  } else {
    AppConfig.Release.TimePassedSinceLastBackupThreshold
  }
  
  private val maxBackupFileCount = if (BuildConfig.DEBUG) {
    AppConfig.Debug.MaxBackupFileCount
  } else {
    AppConfig.Release.MaxBackupFileCount
  }
  
  private val databaseChangesForBackupThreshold = if (BuildConfig.DEBUG) {
    AppConfig.Debug.DatabaseChangesForBackupThreshold
  } else {
    AppConfig.Release.DatabaseChangesForBackupThreshold
  }
  
  override val storageBackupInteractor = StorageBackupInteractor(
    fileSaver = StorageBackupFileSaver(coreModule.application, coreModule.dispatchers),
    preferences = storageBackupPreferences,
    journal = DatabaseChangesJournalImpl(preferencesModule.settingsPreferences),
    timestampProvider = coreModule.timestampProvider,
    dateTimeFormatter = coreModule.dateTimeFormatter,
    passedTimeSinceLastBackupThreshold = passedTimeSinceLastBackupThreshold,
    databaseChangesThreshold = databaseChangesForBackupThreshold,
    backupFileCountThreshold = maxBackupFileCount,
  )
  
  override val showUsernamesInteractor =
      ShowUsernamesInteractor(preferencesModule.settingsPreferences)
}
