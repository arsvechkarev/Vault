package com.arsvechkarev.vault.test.core.ext

import com.arsvechkarev.vault.features.common.AppConstants.DEFAULT_INTERNAL_FILENAME
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import java.io.File

fun VaultAutotestRule.launchActivityWithDatabase(filename: String) {
  val bytes = context.assets.open(filename).readBytes()
  val file = File(targetContext.filesDir, DEFAULT_INTERNAL_FILENAME)
  file.createNewFile()
  file.writeBytes(bytes)
  launchActivity()
}