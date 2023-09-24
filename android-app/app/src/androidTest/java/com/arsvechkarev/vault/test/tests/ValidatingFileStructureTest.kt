package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import buisnesslogic.AesSivTinkCryptography
import buisnesslogic.Password
import buisnesslogic.model.EntriesLists
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
            confirmationDialog.action2.click()
            imageBack.click()
          }
          menu {
            open()
            exportPasswordsMenuItem.click()
          }
          //          KExportPasswordsScreen {
          //            layoutFolder.click()
          //            editTextFilename.replaceText("passwords.kdbx")
          //            buttonExportPasswords.click()
          //            checkEqualExceptIds()
          //          }
        }
      }
    }
  }
  
  private fun checkEqualExceptIds() {
    val expectedBytes = context.assets.open("file_two_passwords_and_plain_text").readBytes()
    val expectedString = AesSivTinkCryptography.decryptData(Password.create("qwetu1233"),
      expectedBytes)
    val actualBytes = stubPasswordsFileExporter.exportedData!!
    val actualString = AesSivTinkCryptography.decryptData(Password.create("qwetu1233"), actualBytes)
    
    val gson = Gson()
    val expectedEntriesLists = gson.fromJson(expectedString, EntriesLists::class.java)
    val actualEntriesLists = gson.fromJson(actualString, EntriesLists::class.java)
    
    assertEquals(expectedEntriesLists.passwords.size, actualEntriesLists.passwords.size)
    expectedEntriesLists.passwords.forEach { expectedPassword ->
      checkNotNull(
        actualEntriesLists.passwords.find { it.title == expectedPassword.title })
          .let { actualPasswordEntry ->
            assertEquals(expectedPassword.username, actualPasswordEntry.username)
            assertEquals(expectedPassword.password, actualPasswordEntry.password)
            assertEquals(expectedPassword.notes, actualPasswordEntry.notes)
          }
    }
    assertEquals(expectedEntriesLists.plainTextEntries.size,
      actualEntriesLists.plainTextEntries.size)
    expectedEntriesLists.plainTextEntries.forEach { expectedPlainText ->
      assertEquals(expectedPlainText.text,
        checkNotNull(
          actualEntriesLists.plainTextEntries.find { it.title == expectedPlainText.title }).text)
    }
    // TODO (1/10/2023): Add credit cards validation, restructure test
    //  to check one item of each type
  }
}
