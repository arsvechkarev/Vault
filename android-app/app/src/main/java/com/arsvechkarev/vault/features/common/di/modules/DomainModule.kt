package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.data.StorageBackupFileSaver
import com.arsvechkarev.vault.features.common.data.StorageBackupPreferences
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournal
import com.arsvechkarev.vault.features.common.domain.DatabaseChangesJournalImpl
import com.arsvechkarev.vault.features.common.domain.ShowUsernamesInteractor
import com.arsvechkarev.vault.features.common.domain.SimpleDateTimeFormatter
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
  
  override val storageBackupInteractor = StorageBackupInteractor(
    fileSaver = StorageBackupFileSaver(coreModule.application, coreModule.dispatchers),
    preferences = storageBackupPreferences,
    journal = DatabaseChangesJournalImpl(preferencesModule.settingsPreferences),
    timestampProvider = coreModule.timestampProvider,
    dateTimeFormatter = SimpleDateTimeFormatter(),
    passedTimeSinceLastBackupThreshold = StorageBackupInteractor.THRESHOLD_PASSED_TIME_SINCE_LAST_BACKUP,
    databaseChangesThreshold = StorageBackupInteractor.THRESHOLD_DATABASE_CHANGES,
  )
  
  override val showUsernamesInteractor =
      ShowUsernamesInteractor(preferencesModule.settingsPreferences)
}
