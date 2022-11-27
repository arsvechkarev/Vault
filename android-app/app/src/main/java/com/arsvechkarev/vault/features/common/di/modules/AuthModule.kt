package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.domain.AuthChecker
import com.arsvechkarev.vault.features.common.domain.AuthCheckerImpl

interface AuthModule {
  val authChecker: AuthChecker
}

class AuthModuleImpl(coreModule: CoreModule) : AuthModule {
  override val authChecker: AuthChecker = AuthCheckerImpl(coreModule.application)
}
