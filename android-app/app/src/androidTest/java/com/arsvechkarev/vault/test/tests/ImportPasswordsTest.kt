package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.import_passwords.ImportPasswordsScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.di.stubs.StubExternalFileReader
import com.arsvechkarev.vault.test.core.ext.context
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KImportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.io.InputStream

class ImportPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubFileReader = StubExternalFileReader(
    uriToMatch = "content://myfolder/passwords.kdbx",
    bytesToRead = {
      context.assets.open("database_two_passwords").use(InputStream::readBytes)
    }
  )
  
  @Test
  fun testImportingPasswordsFromInitialScreen() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        activityResultWrapper = StubActivityResultWrapper(
          stubGetFileUri = "content://myfolder/passwords.kdbx"
        ),
        externalFileReader = stubFileReader
      )
    )
    rule.launchActivity()
  }.run {
    KInitialScreen {
      buttonImportPasswords.click()
      KImportPasswordsScreen {
        currentScreenIs<ImportPasswordsScreen>()
        layoutSelectFile.click()
        buttonImportPasswords.click()
        enterPasswordDialog {
          editText.replaceText("qwetu1233")
          buttonContinue.click()
        }
        KMainListScreen {
          currentScreenIs<MainListScreen>()
          recycler {
            hasSize(3)
            childAt<PasswordItem>(1) {
              text.hasText("google")
//              image.hasDrawable(R.drawable.icon_google)
            }
            childAt<PasswordItem>(2) {
              text.hasText("test.com")
              image.hasDrawable(LetterInCircleDrawable("t"))
            }
          }
        }
      }
    }
  }
  
  @Test
  fun importingPasswordsFromMainMenuTest() = init {
    runBlocking {
      CoreComponentHolder.initialize(
        application = ApplicationProvider.getApplicationContext(),
        factory = StubExtraDependenciesFactory(
          activityResultWrapper = StubActivityResultWrapper(
            stubGetFileUri = "content://myfolder/passwords.kdbx"
          ),
          externalFileReader = stubFileReader
        ),
      )
      rule.launchActivityWithDatabase("database_one_password")
    }
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        recycler {
          hasSize(2)
          childAt<PasswordItem>(1) {
            text.hasText("abc")
          }
        }
        
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
          titleSelectFile.hasText("File")
          textSelectFile.hasText("/storage/emulated/0/passwords.kdbx")
          
          buttonImportPasswords.click()
          
          layoutSelectFile.click()
          
          textSelectFile.hasText("/storage/emulated/0/passwords.kdbx")
          
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
            text.hasText("google")
            //            image.hasDrawable(R.drawable.icon_google)
          }
          childAt<PasswordItem>(2) {
            text.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
}
