package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KChangeMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KSettingsScreen
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChangingMasterPasswordTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_passwords")
    rule.launchActivity()
  }
  
  @Test
  fun testChangingMasterPassword() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          itemChangePassword.click()
          closeSoftKeyboard()
          pressBack()
          pressBack()
        }
        
        currentScreenIs(MainListScreen::class)
        
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          itemChangePassword.click()
          
          enteringPasswordDialog {
            isDisplayed()
            title.hasText("Enter master password to proceed")
            textError.hasEmptyText()
            
            editText.replaceText("abc")
            buttonContinue.click()
            
            textError.hasText("Password is incorrect")
            
            editText.replaceText("qwetu1233")
            
            textError.hasEmptyText()
            
            buttonContinue.click()
          }
          
          KChangeMasterPasswordScreen {
            iconBack.click()
          }
          
          currentScreenIs(SettingsScreen::class)
          
          itemChangePassword.click()
          
          enteringPasswordDialog {
            editText.replaceText("qwetu1233")
            buttonContinue.click()
          }
          
          KChangeMasterPasswordScreen {
            textError.hasEmptyText()
            
            buttonChange.click()
            
            textError.hasEmptyText()
            
            editTextEnterNewPassword.replaceText("qwetu1233")
            
            buttonChange.click()
            
            textError.hasText("Passwords don\'t match")
            editTextEnterNewPassword.hasText("qwetu1233")
            editTextRepeatPassword.hasEmptyText()
            
            editTextRepeatPassword.replaceText("qwetu1233")
            
            textError.hasEmptyText()
            
            buttonChange.click()
            
            textError.hasText("Entered password is the same as current")
            
            editTextEnterNewPassword.replaceText("kjc93pq")
            editTextRepeatPassword.replaceText("kjc93pq")
            
            buttonChange.click()
            
            infoDialog {
              isDisplayed()
              title.hasText("Confirmation")
              message.hasText(R.string.text_confirmation_message)
              action1.hasText("CANCEL")
              action2.hasText("PROCEED")
            }
            
            infoDialog.hide()
            
            infoDialog.isNotDisplayed()
            
            buttonChange.click()
            
            infoDialog.action2.click()
            
            infoDialog {
              isDisplayed()
              title.hasText("Done!")
              message.hasText("Master password successfully changed")
              action1.isNotDisplayed()
              action2.hasText("GOT IT")
            }
            
            infoDialog.action2.click()
          }
          
          currentScreenIs(SettingsScreen::class)
        }
      }
    }
    
    rule.finishActivity()
    
    rule.launchActivity()
    
    KLoginScreen {
      editTextEnterPassword.clearText()
      editTextEnterPassword.typeText("qwetu1233")
      buttonContinue.click()
      
      textError.hasText("Password is incorrect")
      
      editTextEnterPassword.replaceText("kjc93pq")
      buttonContinue.click()
      
      KMainListScreen {
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            text.hasText("google")
            icon.hasDrawable(R.drawable.icon_google)
          }
          childAt<PasswordItem>(2) {
            text.hasText("test.com")
            icon.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
}
