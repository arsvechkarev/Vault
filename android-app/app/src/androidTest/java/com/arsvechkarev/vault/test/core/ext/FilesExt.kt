package com.arsvechkarev.vault.test.core.ext

import buisnesslogic.FileSaver
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.features.common.data.FileSaverImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import com.arsvechkarev.vault.features.common.domain.AuthCheckerImpl
import kotlinx.coroutines.runBlocking

suspend fun writeVaultFileFromAssets(filename: String): FileSaver = runBlocking {
  val fileSaverImpl = FileSaverImpl(
    IoModuleImpl.FILENAME,
    targetContext,
    DefaultDispatchersFacade
  )
  val bytes = context.assets.open(filename).readBytes()
  fileSaverImpl.saveData(bytes)
  return@runBlocking fileSaverImpl
}

fun setUserLoggedIn() {
  AuthCheckerImpl(targetContext).setUserIsLoggedIn()
}
