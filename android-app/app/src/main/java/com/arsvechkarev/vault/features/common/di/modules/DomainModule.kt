package com.arsvechkarev.vault.features.common.di.modules

import buisnesslogic.IdGeneratorImpl
import buisnesslogic.UniqueIdProvideImpl
import buisnesslogic.interactors.KeePassPasswordModelInteractor
import buisnesslogic.interactors.KeePassPlainTextModelInteractor
import com.arsvechkarev.vault.features.common.data.storage.BasicDatabaseStorage
import com.arsvechkarev.vault.features.common.data.storage.CachedDatabaseStorage
import com.arsvechkarev.vault.features.common.data.storage.ObservableCachedDatabaseStorage

interface DomainModule {
  val keePassPasswordModelInteractor: KeePassPasswordModelInteractor
  val keePassPlainTextModelInteractor: KeePassPlainTextModelInteractor
  val observableCachedDatabaseStorage: ObservableCachedDatabaseStorage
}

class DomainModuleImpl(
  ioModule: IoModule,
) : DomainModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordModelInteractor = KeePassPasswordModelInteractor(generator)
  
  override val keePassPlainTextModelInteractor = KeePassPlainTextModelInteractor(generator)
  
  override val observableCachedDatabaseStorage =
      ObservableCachedDatabaseStorage(
        CachedDatabaseStorage(
          BasicDatabaseStorage(ioModule.databaseFileSaver)
        )
      )
}
