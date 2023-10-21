package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.features.common.AppConfig
import com.arsvechkarev.vault.features.common.data.files.StorageBackupExternalFileSaverImpl
import com.arsvechkarev.vault.features.common.data.files.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.domain.BackupInterceptor
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
  preferencesModule: PreferencesModule,
  backupInterceptor: BackupInterceptor,
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
    storageBackupExternalFileSaver = StorageBackupExternalFileSaverImpl(
      coreModule.application,
      coreModule.dispatchers,
      backupInterceptor
    ),
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
