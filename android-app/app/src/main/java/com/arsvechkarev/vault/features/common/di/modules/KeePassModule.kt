package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.data.database.BasicDatabaseStorage
import com.arsvechkarev.vault.features.common.data.database.CachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.database.ObservableCachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.files.DefaultDatabaseFileSaver
import com.arsvechkarev.vault.features.common.data.files.StorageBackupDatabaseFileSaver
import com.arsvechkarev.vault.features.main_list.domain.EntriesListUiMapper
import domain.DEFAULT_INTERNAL_FILENAME
import domain.DatabaseCache
import domain.DatabaseFileSaver
import domain.DatabaseInitializer
import domain.DefaultDatabaseInitializer
import domain.IdGeneratorImpl
import domain.RealInstantProvider
import domain.UniqueIdProvideImpl
import domain.interactors.KeePassPasswordModelInteractor
import domain.interactors.KeePassPlainTextModelInteractor

interface KeePassModule {
  val keePassPasswordModelInteractor: KeePassPasswordModelInteractor
  val keePassPlainTextModelInteractor: KeePassPlainTextModelInteractor
  val databaseCache: DatabaseCache
  val databaseFileSaver: DatabaseFileSaver
  val databaseInitializer: DatabaseInitializer
  val observableCachedDatabaseStorage: ObservableCachedDatabaseStorage
  val entriesListUiMapper: EntriesListUiMapper
}

class KeePassModuleImpl(
  coreModule: CoreModule,
  domainModule: DomainModule
) : KeePassModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordModelInteractor = KeePassPasswordModelInteractor(
    generator,
    RealInstantProvider
  )
  
  override val keePassPlainTextModelInteractor = KeePassPlainTextModelInteractor(
    generator,
    RealInstantProvider
  )
  
  override val databaseFileSaver = StorageBackupDatabaseFileSaver(
    databaseFileSaver = DefaultDatabaseFileSaver(
      DEFAULT_INTERNAL_FILENAME,
      coreModule.application,
      coreModule.dispatchers,
      coreModule.globalIOScope,
    ),
    databaseChangesJournal = domainModule.databaseChangesJournal,
    storageBackupInteractor = domainModule.storageBackupInteractor,
    scope = coreModule.globalIOScope
  )
  
  private val cachedDatabaseStorage = CachedDatabaseStorage(
    BasicDatabaseStorage(databaseFileSaver)
  )
  
  override val databaseCache = cachedDatabaseStorage
  
  override val databaseInitializer = DefaultDatabaseInitializer(databaseCache, databaseFileSaver)
  
  override val observableCachedDatabaseStorage =
      ObservableCachedDatabaseStorage(cachedDatabaseStorage)
  
  override val entriesListUiMapper = EntriesListUiMapper()
}
