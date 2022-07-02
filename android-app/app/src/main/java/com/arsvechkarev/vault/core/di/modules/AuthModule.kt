package com.arsvechkarev.vault.core.di.modules

import com.arsvechkarev.vault.core.AuthChecker
import com.arsvechkarev.vault.core.AuthCheckerImpl

interface AuthModule {
  val authChecker: AuthChecker
}

class AuthModuleImpl(coreModule: CoreModule) : AuthModule {
  override val authChecker: AuthChecker = AuthCheckerImpl(coreModule.application)
}
