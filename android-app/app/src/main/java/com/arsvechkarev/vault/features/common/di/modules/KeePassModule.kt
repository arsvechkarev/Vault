package com.arsvechkarev.vault.features.common.di.modules

import domain.IdGeneratorImpl
import domain.RealInstantProvider
import domain.UniqueIdProvideImpl
import domain.interactors.KeePassPasswordEntryInteractor
import domain.interactors.KeePassNoteEntryInteractor

interface KeePassModule {
  val keePassPasswordEntryInteractor: KeePassPasswordEntryInteractor
  val keePassNoteEntryInteractor: KeePassNoteEntryInteractor
}

class KeePassModuleImpl : KeePassModule {
  
  private val generator = UniqueIdProvideImpl(IdGeneratorImpl)
  
  override val keePassPasswordEntryInteractor = KeePassPasswordEntryInteractor(
    generator,
    RealInstantProvider
  )
  
  override val keePassNoteEntryInteractor = KeePassNoteEntryInteractor(
    generator,
    RealInstantProvider
  )
}
