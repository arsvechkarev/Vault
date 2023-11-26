package com.arsvechkarev.vault.test.core.ext

import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.encode
import com.arsvechkarev.vault.features.common.AppConstants.DEFAULT_INTERNAL_FILENAME
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import timber.log.Timber
import java.io.File
import java.io.InputStream
import java.util.Base64

fun VaultAutotestRule.launchActivityWithDatabase(filename: String) {
  val bytes = context.assets.open(filename).use(InputStream::readBytes)
  val file = File(targetContext.filesDir, DEFAULT_INTERNAL_FILENAME)
  file.createNewFile()
  val encodedBytes = Base64.getEncoder().encodeToString(bytes)
  Timber.tag("qqq").d(encodedBytes)
  file.writeBytes(bytes)
  launchActivity()
}

fun VaultAutotestRule.launchActivityWithDatabase(database: KeePassDatabase) {
  val file = File(targetContext.filesDir, DEFAULT_INTERNAL_FILENAME)
  file.createNewFile()
  file.outputStream().use(database::encode)
  launchActivity()
}
