package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.model.Entries
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultSubstitutor
import com.arsvechkarev.vault.test.core.stub.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.screens.KCreatingMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KCreatingPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KExportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KPasswordInfoScreen
import com.google.gson.Gson
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ValidatingFileStructureTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubPasswordsFileExporter = StubPasswordsFileExporter()
  
  @Before
  fun setup() {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      activityResultSubstitutor = StubActivityResultSubstitutor(
        stubSelectedFolderUri = "content://myfolder",
        stubCreatedFileUri = "content://myfolder/myfile.png"
      ),
      passwordsFileExporter = stubPasswordsFileExporter
    )
    rule.launchActivity()
  }
  
  
  @Test
  fun testValidatingFileStructure() = run {
    KInitialScreen {
      buttonCreateMasterPassword.click()
      KCreatingMasterPasswordScreen {
        editTextEnterPassword.replaceText("qwetu1233")
        buttonContinue.click()
        editTextRepeatPassword.replaceText("qwetu1233")
        buttonContinue.click()
        KMainListScreen {
          menu {
            open()
            newPasswordMenuItem.click()
          }
          entryTypeDialog.passwordEntry.click()
          KCreatingPasswordEntryScreen {
            editTextWebsiteName.replaceText("google")
            editTextLogin.replaceText("me@gmail.com")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("F/<1#E(J=\\51=k")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KPasswordInfoScreen {
                iconBack.click()
              }
            }
          }
          menu {
            open()
            newPasswordMenuItem.click()
          }
          entryTypeDialog.passwordEntry.click()
          KCreatingPasswordEntryScreen {
            editTextWebsiteName.replaceText("test.com")
            editTextLogin.replaceText("abcd")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("q3z;ob15/*8GK>Ed")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KPasswordInfoScreen {
                iconBack.click()
              }
            }
          }
          menu {
            open()
            exportPasswordsMenuItem.click()
          }
          KExportPasswordsScreen {
            layoutFolder.click()
            editTextFilename.replaceText("myfile.png")
            buttonExportPasswords.click()
            checkEqualExceptIds()
          }
        }
      }
    }
  }
  
  private fun checkEqualExceptIds() {
    val expectedBytes = context.assets.open("file_two_items").readBytes()
    val expectedString = AesSivTinkCryptography.decryptData("qwetu1233", expectedBytes)
    val actualBytes = stubPasswordsFileExporter.exportedData!!
    val actualString = AesSivTinkCryptography.decryptData("qwetu1233", actualBytes)
  
    val gson = Gson()
    val expectedEntries = gson.fromJson(expectedString, Entries::class.java)
    val actualEntries = gson.fromJson(actualString, Entries::class.java)
  
    // TODO (1/10/2023): Add credit cards and plain texts validation
    assertEquals(expectedEntries.passwords.size, actualEntries.passwords.size)
    expectedEntries.passwords.forEach { expectedPassword ->
      checkNotNull(actualEntries.passwords.find { it.websiteName == expectedPassword.websiteName })
          .let { actualPasswordInfo ->
            assertEquals(expectedPassword.login, actualPasswordInfo.login)
            assertEquals(expectedPassword.password, actualPasswordInfo.password)
            assertEquals(expectedPassword.notes, actualPasswordInfo.notes)
          }
    }
  }
}
