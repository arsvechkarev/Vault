package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.test.core.VaultAutotestRule
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.setUserLoggedIn
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportingPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_items")
    setUserLoggedIn()
    rule.launchActivity()
  }
  
  @Test
  fun testExportingPasswords() {
  
  }
}
