package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import app.keemobile.kotpass.cryptography.EncryptedValue
import app.keemobile.kotpass.database.Credentials
import app.keemobile.kotpass.database.KeePassDatabase
import app.keemobile.kotpass.database.decode
import app.keemobile.kotpass.database.findEntries
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
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
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_PLAIN_TEXT
import domain.CUSTOM_DATA_TYPE_KEY
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream

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
            editTextNotes.replaceText("lorem ipsum")
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
          entryTypeDialog.plainTextEntry.click()
          KPlainTextEntryScreen {
            editTextTitle.replaceText("my title")
            editTextText.replaceText("my text")
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
    val expectedBytes = context.assets.open("file_one_password_and_plain_text")
        .use(InputStream::readBytes)
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
            
            assertEquals(expectedEntry.customData.getValue(CUSTOM_DATA_FAVORITE_KEY).value,
              actualEntry.customData.getValue(CUSTOM_DATA_FAVORITE_KEY).value)
            
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
  
  @Test
  fun testingFileFromOneKeePass() = init {
    rule.launchActivityWithDatabase("file_from_one_kee_pass")
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
