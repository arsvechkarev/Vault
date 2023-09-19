package com.arsvechkarev.vault.test.core.ext

import buisnesslogic.DatabaseSaver
import com.arsvechkarev.vault.core.DefaultDispatchersFacade
import com.arsvechkarev.vault.features.common.data.DatabaseSaverImpl
import com.arsvechkarev.vault.features.common.di.modules.IoModuleImpl
import kotlinx.coroutines.runBlocking

suspend fun writeVaultFileFromAssets(filename: String): DatabaseSaver = runBlocking {
  val fileSaverImpl = DatabaseSaverImpl(
    IoModuleImpl.FILENAME,
    targetContext,
    DefaultDispatchersFacade
  )
  val bytes = context.assets.open(filename).readBytes()
  fileSaverImpl.save(bytes)
  return@runBlocking fileSaverImpl
}
