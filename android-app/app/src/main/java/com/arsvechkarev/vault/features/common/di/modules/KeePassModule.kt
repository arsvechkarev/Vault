package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.DatabaseCache
import buisnesslogic.DatabaseFileSaver
import buisnesslogic.DatabaseInitializer
import buisnesslogic.DefaultDatabaseInitializer
import buisnesslogic.IdGeneratorImpl
import buisnesslogic.UniqueIdProvideImpl
import buisnesslogic.interactors.KeePassPasswordModelInteractor
import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import com.arsvechkarev.vault.features.common.data.DatabaseFileSaverImpl
import com.arsvechkarev.vault.features.common.data.storage.BasicDatabaseStorage
import com.arsvechkarev.vault.features.common.data.storage.CachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage

interface KeePassModule {
  val keePassPasswordModelInteractor: KeePassPasswordModelInteractor
  val keePassPlainTextModelInteractor: KeePassPlainTextModelInteractor
  val databaseCache: DatabaseCache
  val databaseFileSaver: DatabaseFileSaver
  val databaseInitializer: DatabaseInitializer
  val observableCachedDatabaseStorage: ObservableCachedDatabaseStorage
}

class KeePassModuleImpl(coreModule: CoreModule) : KeePassModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordModelInteractor = KeePassPasswordModelInteractor(generator)
  
  override val keePassPlainTextModelInteractor = KeePassPlainTextModelInteractor(generator)
  
  override val databaseFileSaver = DatabaseFileSaverImpl(
    IoModuleImpl.FILENAME,
    coreModule.application,
    coreModule.dispatchersFacade
  )
  
  private val cachedDatabaseStorage = CachedDatabaseStorage(
    BasicDatabaseStorage(databaseFileSaver)
  )
  
  override val databaseCache = cachedDatabaseStorage
  
  override val databaseInitializer = DefaultDatabaseInitializer(databaseCache, databaseFileSaver)
  
  override val observableCachedDatabaseStorage =
      ObservableCachedDatabaseStorage(cachedDatabaseStorage)
}