package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.domain.ChangeMasterPasswordObserver
import com.arsvechkarev.vault.features.common.domain.ChangeMasterPasswordObserverImpl
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordSetupObserver
import com.arsvechkarev.vault.features.common.domain.CreatingPasswordSetupObserverImpl
import com.arsvechkarev.vault.features.common.domain.PasswordObserver
import com.arsvechkarev.vault.features.common.domain.PasswordObserverImpl

interface ObserversModule {
  val changeMasterPasswordObserver: ChangeMasterPasswordObserver
  val passwordObserver: PasswordObserver
  val creatingPasswordSetupObserver: CreatingPasswordSetupObserver
}

class ObserversModuleImpl : ObserversModule {
  override val changeMasterPasswordObserver = ChangeMasterPasswordObserverImpl()
  override val passwordObserver = PasswordObserverImpl()
  override val creatingPasswordSetupObserver = CreatingPasswordSetupObserverImpl()
}