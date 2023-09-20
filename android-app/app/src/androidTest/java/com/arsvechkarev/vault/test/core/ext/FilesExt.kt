package com.arsvechkarev.vault.test.core.ext

import buisnesslogic.DatabaseFileSaver
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.features.common.data.DatabaseFileSaverImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import kotlinx.coroutines.runBlocking

suspend fun writeVaultFileFromAssets(filename: String): DatabaseFileSaver = runBlocking {
  val fileSaverImpl = DatabaseFileSaverImpl(
    IoModuleImpl.FILENAME,
    targetContext,
    DefaultDispatchersFacade
  )
  val bytes = context.assets.open(filename).readBytes()
  fileSaverImpl.save(bytes)
  return@runBlocking fileSaverImpl
}
