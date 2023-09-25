package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.findEntries
import buisnesslogic.CUSTOM_DATA_PASSWORD
import buisnesslogic.CUSTOM_DATA_PLAIN_TEXT
import buisnesslogic.CUSTOM_DATA_TYPE_KEY
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.stub.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.screens.KCreatingPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream

class ValidatingFileStructureTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubPasswordsFileExporter = StubPasswordsFileExporter()
  
  @Before
  fun setup() {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      activityResultWrapper = StubActivityResultWrapper(
        stubSelectedFolderUri = "content://myfolder",
        stubCreatedFileUri = "content://myfolder/passwords.kdbx"
      ),
      passwordsFileExporter = stubPasswordsFileExporter
    )
    rule.launchActivity()
  }
  
  
  @Test
  fun testValidatingFileStructure() = run {
    KInitialScreen {
      buttonCreateMasterPassword.click()
      KMasterPasswordScreen {
        editTextEnterPassword.replaceText("qwetu1233")
        buttonContinue.click()
        editTextRepeatPassword.replaceText("qwetu1233")
        buttonContinue.click()
        KMainListScreen {
          menu {
            open()
            newEntryMenuItem.click()
          }
          entryTypeDialog.passwordEntry.click()
          KCreatingPasswordEntryScreen {
            editTextTitle.replaceText("google")
            editTextUsername.replaceText("me@gmail.com")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("F/<1#E(J=\\51=k;")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KPasswordEntryScreen {
                imageBack.click()
              }
            }
          }
          menu {
            open()
            newEntryMenuItem.click()
          }
          entryTypeDialog.passwordEntry.click()
          KCreatingPasswordEntryScreen {
            editTextTitle.replaceText("test.com")
            editTextUsername.replaceText("abcd")
            buttonContinue.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("q3z;ob15/*8GK>Ed")
              buttonSavePassword.click()
              confirmationDialog.action2.click()
              KPasswordEntryScreen {
                imageBack.click()
              }
            }
          }
          menu {
            open()
            newEntryMenuItem.click()
          }
          entryTypeDialog.plainTextEntry.click()
          KPlainTextEntryScreen {
            editTextTitle.replaceText("my title")
            editTextText.replaceText("super secret content")
            buttonSave.click()
            imageBack.click()
          }
          menu {
            open()
            exportPasswordsMenuItem.click()
          }
          checkEqualExceptIds()
        }
      }
    }
  }
  
  private fun checkEqualExceptIds() {
    val expectedBytes = context.assets.open("database_two_passwords_and_plain_text").readBytes()
    val expectedDatabase = KeePassDatabase.decode(ByteArrayInputStream(expectedBytes),
      Credentials.from(EncryptedValue.fromString("qwetu1233")))
    val actualDatabase = checkNotNull(stubPasswordsFileExporter.exportedDatabase)
    
    val expectedEntriesList = expectedDatabase.findEntries { true }.flatMap { it.second }
    val actualEntriesList = actualDatabase.findEntries { true }.flatMap { it.second }
    assertEquals(expectedEntriesList.size, actualEntriesList.size)
    expectedEntriesList.forEach { expectedEntry ->
      checkNotNull(actualEntriesList.find { it.fields.title == expectedEntry.fields.title })
          .let { actualEntry ->
            assertEquals(expectedEntry.fields.userName?.content,
              actualEntry.fields.userName?.content)
            assertEquals(expectedEntry.fields.password?.content,
              actualEntry.fields.password?.content)
            assertEquals(expectedEntry.fields.notes?.content, actualEntry.fields.notes?.content)
            
            val expectedEntryType = expectedEntry.customData.getValue(CUSTOM_DATA_TYPE_KEY).value
            val actualEntryType = actualEntry.customData.getValue(CUSTOM_DATA_TYPE_KEY).value
            
            if (expectedEntryType != CUSTOM_DATA_PASSWORD
                && expectedEntryType != CUSTOM_DATA_PLAIN_TEXT) {
              throw AssertionError(
                "Custom data type of entry $expectedEntry is [$expectedEntryType]"
              )
            }
            
            assertEquals(expectedEntryType, actualEntryType)
          }
    }
  }
}
