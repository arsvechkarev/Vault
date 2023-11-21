package com.arsvechkarev.vault.features.common.di.modules

import domain.IdGeneratorImpl
import domain.RealInstantProvider
import domain.UniqueIdProvideImpl
import domain.interactors.KeePassPasswordModelInteractor
import domain.interactors.KeePassPlainTextModelInteractor

interface KeePassModule {
  val keePassPasswordModelInteractor: KeePassPasswordModelInteractor
  val keePassPlainTextModelInteractor: KeePassPlainTextModelInteractor
}

class KeePassModuleImpl : KeePassModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordModelInteractor = KeePassPasswordModelInteractor(
    generator,
    RealInstantProvider
  )
  
  override val keePassPlainTextModelInteractor = KeePassPlainTextModelInteractor(
    generator,
    RealInstantProvider
  )
}
