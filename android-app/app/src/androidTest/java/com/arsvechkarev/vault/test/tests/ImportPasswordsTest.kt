package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import app.keemobile.kotpass.database.encode
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.data.Databases
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.di.stubs.StubExternalFileReader
import com.arsvechkarev.vault.test.core.di.stubs.TestImageRequestsRecorder
import com.arsvechkarev.vault.test.core.di.stubs.URL_IMAGE_GOOGLE
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.ext.wasImageRequestWithUrlPerformed
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KImportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.util.Base64

class ImportPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubFileReader = StubExternalFileReader(
    uriToMatch = "content://myfolder/passwords.kdbx",
    bytesToRead = {
      val stream = ByteArrayOutputStream()
      Databases.TwoPasswords.encode(stream)
      stream.toByteArray()
    }
  )
  
  private val testImageRequestsRecorder = TestImageRequestsRecorder()
  
  @Before
  fun setup() {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        activityResultWrapper = StubActivityResultWrapper(
          stubSelectPasswordsFileUri = "content://myfolder/passwords.kdbx",
        ),
        externalFileReader = stubFileReader,
        imagesRequestsRecorder = testImageRequestsRecorder
      ),
    )
  }
  
  @Test
  fun testImportingPasswordsFromInitialScreen() = init {
    rule.launchActivity()
  }.run {
    KInitialScreen {
      buttonImportPasswords.click()
      
      KImportPasswordsScreen {
        buttonImportPasswords.click()
        
        enterPasswordDialog {
          title.hasText("Enter password")
          editText.replaceText("qwetu1233")
          buttonContinue.click()
        }
        
        KMainListScreen {
          recycler {
            hasSize(3)
            childAt<PasswordItem>(1) {
              title.hasText("google")
              image.wasImageRequestWithUrlPerformed(URL_IMAGE_GOOGLE, testImageRequestsRecorder)
            }
            childAt<PasswordItem>(2) {
              title.hasText("test.com")
              image.hasDrawable(LetterInCircleDrawable("t"))
            }
          }
        }
      }
    }
  }
  
  @Test
  fun testImportingPasswordsFromMainMenu() = init {
    rule.launchActivityWithDatabase(Databases.TwoPasswords)
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        recycler.hasSize(3)
        
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          currentScreenIs<ImportPasswordsScreen>()
          imageBack.click()
        }
        
        currentScreenIs<MainListScreen>()
        
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          titleSelectPasswordsFile.hasText("Passwords file")
          textSelectPasswordsFile.hasText("/storage/emulated/0/passwords.kdbx")
          
          buttonImportPasswords.click()
          
          infoDialog {
            isDisplayed()
            title.hasText("Confirmation")
            message.hasText(R.string.text_confirm_import_passwords_message)
            action1.hasText("CANCEL")
            action2.hasText("CONFIRM")
          }
          
          infoDialog.action1.click()
          
          infoDialog.isNotDisplayed()
          
          buttonImportPasswords.click()
          
          infoDialog.isDisplayed()
          
          closeSoftKeyboard()
          pressBack()
          
          infoDialog.isNotDisplayed()
          
          buttonImportPasswords.click()
          infoDialog.action2.click()
          
          enterPasswordDialog.isDisplayed()
          
          enterPasswordDialog.imageCross.click()
          
          enterPasswordDialog.isNotDisplayed()
          
          buttonImportPasswords.click()
          infoDialog.action2.click()
          
          enterPasswordDialog {
            title.hasText("Enter password for decryption")
            textError.hasEmptyText()
            
            editText.replaceText("abc")
            buttonContinue.click()
          }
          
          infoDialog {
            isDisplayed()
            title.hasText("Error")
            message.hasText(R.string.text_error_import_message)
            action1.isNotDisplayed()
            action2.hasText("OK")
          }
          
          infoDialog.action2.click()
          
          infoDialog.isNotDisplayed()
          
          enterPasswordDialog.isDisplayed()
          
          enterPasswordDialog {
            editText.replaceText("qwetu1233")
            buttonContinue.click()
          }
        }
        
        currentScreenIs<MainListScreen>()
        
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            title.hasText("google")
            image.wasImageRequestWithUrlPerformed(URL_IMAGE_GOOGLE, testImageRequestsRecorder)
          }
          childAt<PasswordItem>(2) {
            title.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
  
  @Test
  fun testImportingPasswordsFromMainMenuWhenEmpty() = init {
    rule.launchActivityWithDatabase(Databases.Empty)
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          viewSelectPasswordsFile.click()
          buttonImportPasswords.click()
          
          enterPasswordDialog {
            title.hasText("Enter password")
            editText.replaceText("qwetu1233")
            buttonContinue.click()
          }
        }
        
        currentScreenIs<MainListScreen>()
        
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            title.hasText("google")
            image.wasImageRequestWithUrlPerformed(URL_IMAGE_GOOGLE, testImageRequestsRecorder)
          }
          childAt<PasswordItem>(2) {
            title.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
  
  @Test
  fun testImportingWithAKeyFile() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        activityResultWrapper = StubActivityResultWrapper(
          stubSelectPasswordsFileUri = "content://myfolder/passwords.kdbx",
          stubSelectKeyFileUri = "content://myfolder/keyfile.kf"
        ),
        externalFileReader = StubExternalFileReader(
          bytesToRead = { uri ->
            when {
              uri.toString().endsWith("kf") -> {
                Base64.getDecoder().decode(Databases.EncodedKeyFile)
              }
              uri.toString().endsWith("kdbx") -> {
                Base64.getDecoder().decode(Databases.EncodedDatabaseFromKeePassWithKeyFile)
              }
              else -> error("Unknown uri: $uri")
            }
          }
        ),
        imagesRequestsRecorder = testImageRequestsRecorder
      ),
    )
    rule.launchActivity()
  }.run {
    KInitialScreen {
      buttonImportPasswords.click()
      
      KImportPasswordsScreen {
        titleSelectKeyFile.hasText("Key file (optional)")
        textSelectKeyFile.hasText("Select key file")
        imageClearKeyFile.isNotDisplayed()
        
        buttonImportPasswords.click()
        
        enterPasswordDialog {
          editText.replaceText("qwetu1233")
          buttonContinue.click()
        }
        
        infoDialog {
          isDisplayed()
          title.hasText("Error")
        }
        
        infoDialog.action2.click()
        
        infoDialog.isNotDisplayed()
        
        enterPasswordDialog.imageCross.click()
        
        enterPasswordDialog.isNotDisplayed()
        
        viewSelectKeyFile.click()
        
        textSelectKeyFile.hasText("/storage/emulated/0/keyfile.kf")
        
        imageClearKeyFile.isDisplayed()
        imageClearKeyFile.click()
        
        textSelectKeyFile.hasText("Select key file")
        
        viewSelectKeyFile.click()
        
        buttonImportPasswords.click()
        
        enterPasswordDialog {
          editText.replaceText("qwetu1233")
          buttonContinue.click()
        }
        
        KMainListScreen {
          recycler {
            hasSize(3)
            childAt<PasswordItem>(1) {
              title.hasText("supertest")
            }
            childAt<PasswordItem>(2) {
              title.hasText("title1")
            }
          }
        }
      }
    }
  }
}
