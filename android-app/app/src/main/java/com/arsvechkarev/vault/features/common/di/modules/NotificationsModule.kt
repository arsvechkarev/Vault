package com.arsvechkarev.vault.features.common.di.modules

import com.arsvechkarev.vault.features.common.domain.GlobalChangeMasterPasswordImpl
import com.arsvechkarev.vault.features.common.domain.GlobalChangeMasterPasswordPublisher
import com.arsvechkarev.vault.features.common.domain.GlobalChangeMasterPasswordSubscriber

interface NotificationsModule {
  val globalChangeMasterPasswordSubscriber: GlobalChangeMasterPasswordSubscriber
  val globalChangeMasterPasswordPublisher: GlobalChangeMasterPasswordPublisher
}

class NotificationsModuleImpl : NotificationsModule {
  private val globalChangeMasterPasswordImpl = GlobalChangeMasterPasswordImpl()
  override val globalChangeMasterPasswordSubscriber = globalChangeMasterPasswordImpl
  override val globalChangeMasterPasswordPublisher = globalChangeMasterPasswordImpl
}