package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.BuildConfig
import com.arsvechkarev.vault.features.common.AppConfig
import com.arsvechkarev.vault.features.common.AppConstants
import com.arsvechkarev.vault.features.common.data.database.BasicDatabaseStorage
import com.arsvechkarev.vault.features.common.data.database.CachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.files.DefaultDatabaseFileSaver
import com.arsvechkarev.vault.features.common.data.files.StorageBackupDatabaseFileSaver
import com.arsvechkarev.vault.features.common.data.files.StorageBackupExternalFileSaverImpl
import com.arsvechkarev.vault.features.common.data.files.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.domain.BackupInterceptor
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournalImpl
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor
import com.arsvechkarev.vault.features.common.domain.StorageBackupInteractor
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import com.arsvechkarev.vault.features.main_list.domain.LoadEntriesInteractor
import domain.DatabaseCache
import domain.DatabaseFileSaver
import domain.DatabaseInitializer
import domain.DefaultDatabaseInitializer

interface DomainModule {
  val storageBackupPreferences: StorageBackupPreferences
  val storageBackupInteractor: StorageBackupInteractor
  val databaseFileSaver: DatabaseFileSaver
  val databaseCache: DatabaseCache
  val databaseInitializer: DatabaseInitializer
  val observableCachedDatabaseStorage: ObservableCachedDatabaseStorage
  val showUsernamesInteractor: ShowUsernamesInteractor
  val entriesListUiMapper: EntriesListUiMapper
  val loadEntriesInteractor: LoadEntriesInteractor
}

class DomainModuleImpl(
  coreModule: CoreModule,
  ioModule: IoModule,
  preferencesModule: PreferencesModule,
  backupInterceptor: BackupInterceptor,
) : DomainModule {
  
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
  
  override val storageBackupPreferences = StorageBackupPreferences(
    preferencesModule.settingsPreferences
  )
  
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
  
  override val databaseFileSaver = StorageBackupDatabaseFileSaver(
    databaseFileSaver = DefaultDatabaseFileSaver(
      AppConstants.DEFAULT_INTERNAL_PASSWORDS_FILE_NAME,
      ioModule.keyFileSaver,
      coreModule.application,
      coreModule.dispatchers,
      coreModule.globalIOScope,
    ),
    databaseChangesJournal = DatabaseChangesJournalImpl(preferencesModule.settingsPreferences),
    storageBackupInteractor = storageBackupInteractor,
    scope = coreModule.globalIOScope
  )
  
  private val cachedDatabaseStorage = CachedDatabaseStorage(
    BasicDatabaseStorage(databaseFileSaver)
  )
  
  override val databaseCache = cachedDatabaseStorage
  
  override val databaseInitializer = DefaultDatabaseInitializer(databaseCache, databaseFileSaver)
  
  override val observableCachedDatabaseStorage =
      ObservableCachedDatabaseStorage(cachedDatabaseStorage)
  
  override val showUsernamesInteractor =
      ShowUsernamesInteractor(preferencesModule.settingsPreferences)
  
  override val entriesListUiMapper = EntriesListUiMapper()
  
  override val loadEntriesInteractor = LoadEntriesInteractor(
    observableCachedDatabaseStorage,
    entriesListUiMapper,
    showUsernamesInteractor
  )
}
