package com.arsvechkarev.vault.features.common.di.modules

import domain.IdGeneratorImpl
import domain.RealInstantProvider
import domain.UniqueIdProvideImpl
import domain.interactors.KeePassPasswordModelInteractor
import domain.interactors.KeePassNoteModelInteractor

interface KeePassModule {
  val keePassPasswordModelInteractor: KeePassPasswordModelInteractor
  val keePassNoteModelInteractor: KeePassNoteModelInteractor
}

class KeePassModuleImpl : KeePassModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordModelInteractor = KeePassPasswordModelInteractor(
    generator,
    RealInstantProvider
  )
  
  override val keePassNoteModelInteractor = KeePassNoteModelInteractor(
    generator,
    RealInstantProvider
  )
}
