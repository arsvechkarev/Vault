package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.findEntries
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.test.core.data.Databases
import com.arsvechkarev.vault.test.core.data.Databases.PasswordQwetu1233
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.di.stubs.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KNoteEntryScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_NOTE
import domain.CUSTOM_DATA_TYPE_KEY
import domain.from
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.Base64

class ValidatingFileStructureTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubPasswordsFileExporter = StubPasswordsFileExporter()
  
  @Test
  fun testValidatingFileStructure() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        activityResultWrapper = StubActivityResultWrapper(
          stubSelectedFolderUri = "content://myfolder",
          stubCreatedFileUri = "content://myfolder/passwords.kdbx"
        ),
        passwordsFileExporter = stubPasswordsFileExporter
      )
    )
    rule.launchActivity()
  }.run {
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
          KPasswordEntryScreen {
            editTextTitle.replaceText("google")
            editTextUsername.replaceText("me@gmail.com")
            editTextUrl.replaceText("google.com")
            imageEditPassword.click()
            KCreatingPasswordScreen {
              editTextPassword.replaceText("F/<1#E(J=\\51=k;")
              buttonSavePassword.click()
            }
            buttonSave.click()
            imageBack.click()
          }
          menu {
            open()
            newEntryMenuItem.click()
          }
          entryTypeDialog.noteEntry.click()
          KNoteEntryScreen {
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
    val expectedEntriesList = Databases.OnePasswordAndNote.findEntries { true }.flatMap { it.second }
    val actualDatabase = checkNotNull(stubPasswordsFileExporter.exportedDatabase)
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
            
            assertEquals(expectedEntry.customData.getValue(CUSTOM_DATA_FAVORITE_KEY).value,
              actualEntry.customData.getValue(CUSTOM_DATA_FAVORITE_KEY).value)
            
            val expectedEntryType = expectedEntry.customData.getValue(CUSTOM_DATA_TYPE_KEY).value
            val actualEntryType = actualEntry.customData.getValue(CUSTOM_DATA_TYPE_KEY).value
            if (expectedEntryType != CUSTOM_DATA_PASSWORD
                && expectedEntryType != CUSTOM_DATA_NOTE) {
              throw AssertionError(
                "Custom data type of entry $expectedEntry is [$expectedEntryType]"
              )
            }
            assertEquals(expectedEntryType, actualEntryType)
          }
    }
  }
  
  @Test
  fun testingFileFromOneKeePass() = init {
    val bytes = Base64.getDecoder().decode(Databases.EncodedDatabaseFromKeePass)
    val credentials = Credentials.from(PasswordQwetu1233)
    val database = KeePassDatabase.decode(ByteArrayInputStream(bytes), credentials)
    rule.launchActivityWithDatabase(database)
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler {
          hasSize(4)
          childAt<PasswordItem>(1) {
            title.hasText("example")
          }
          childAt<PasswordItem>(2) {
            title.hasText("Some")
          }
          childAt<PasswordItem>(3) {
            title.hasText("test2")
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          editTextTitle.hasText("example")
          editTextUsername.hasText("username")
          imageEditPassword.click()
          KCreatingPasswordScreen {
            editTextPassword.hasText("De\\THpGYpaSb+LW\\E~t!S_Cz==qUk853")
            iconCross.click()
          }
          editTextUrl.hasText("example.com")
          editTextNotes.hasText("thenotes")
          
          pressBack()
        }
        
        recycler.emptyChildAt(2) { click() }
        
        KPasswordEntryScreen {
          editTextTitle.hasText("Some")
          editTextUsername.hasEmptyText()
          textPassword.hasText("Generate password")
          editTextUrl.hasEmptyText()
          editTextNotes.hasEmptyText()
          
          pressBack()
        }
        
        recycler.emptyChildAt(3) { click() }
        
        KPasswordEntryScreen {
          editTextTitle.hasText("test2")
          editTextUsername.hasText("qwerty")
          imageEditPassword.click()
          KCreatingPasswordScreen {
            editTextPassword.hasText("-")
            iconCross.click()
          }
          editTextUrl.hasEmptyText()
          editTextNotes.hasEmptyText()
        }
      }
    }
  }
}
