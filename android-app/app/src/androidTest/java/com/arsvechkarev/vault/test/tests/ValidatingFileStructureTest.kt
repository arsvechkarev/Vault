package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.model.PasswordInfo
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultSubstitutor
import com.arsvechkarev.vault.test.core.stub.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.screens.KCreatingEntryScreen
import com.arsvechkarev.vault.test.screens.KCreatingMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KExportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KInfoScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
          KCreatingEntryScreen {
            editTextWebsiteName.replaceText("google")
            editTextLogin.replaceText("me@gmail.com")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("F/<1#E(J=\\51=k")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KInfoScreen {
                iconBack.click()
              }
            }
          }
          menu {
            open()
            newPasswordMenuItem.click()
          }
          KCreatingEntryScreen {
            editTextWebsiteName.replaceText("test.com")
            editTextLogin.replaceText("abcd")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("2ban1yV41&=z%\$Fy")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KInfoScreen {
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
    val type = TypeToken.getParameterized(List::class.java, PasswordInfo::class.java).type
    val expectedPasswords = gson.fromJson<List<PasswordInfo>>(expectedString, type)
    val actualPasswords = gson.fromJson<List<PasswordInfo>>(actualString, type)
    
    assertEquals(expectedPasswords.size, actualPasswords.size)
    expectedPasswords.forEach { expectedPasswordInfo ->
      checkNotNull(actualPasswords.find { it.websiteName == expectedPasswordInfo.websiteName })
          .let { actualPasswordInfo ->
            assertEquals(expectedPasswordInfo.login, actualPasswordInfo.login)
            assertEquals(expectedPasswordInfo.password, actualPasswordInfo.password)
            assertEquals(expectedPasswordInfo.notes, actualPasswordInfo.notes)
          }
    }
  }
}
