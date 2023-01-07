package com.arsvechkarev.vault.test.tests

import buisnesslogic.PasswordStrength.SECURE
import buisnesslogic.PasswordStrength.WEAK
import buisnesslogic.model.PasswordCharacteristic.NUMBERS
import buisnesslogic.model.PasswordCharacteristic.SPECIAL_SYMBOLS
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.creating_entry.CreatingEntryScreen
import com.arsvechkarev.vault.test.core.VaultAutotestRule
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasPasswordWithAllCharacteristics
import com.arsvechkarev.vault.test.core.ext.hasPasswordWithCharacteristics
import com.arsvechkarev.vault.test.core.ext.hasPasswordWithNoCharacteristics
import com.arsvechkarev.vault.test.core.ext.hasTextColorInt
import com.arsvechkarev.vault.test.core.ext.hasTextLength
import com.arsvechkarev.vault.test.core.ext.setUserLoggedIn
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.screens.KCreatingEntryScreen
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KInfoScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.EmptyItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.viewbuilding.Colors
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreatingEntryTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_no_items")
    setUserLoggedIn()
    rule.launchActivity()
  }
  
  @Test
  fun testCreatingEntry() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
    }
    KMainListScreen {
      recycler {
        hasSize(1)
        firstChild<EmptyItem> {
          image.isDisplayed()
          title.isDisplayed()
          message.isDisplayed()
        }
      }
      
      menu {
        open()
        newPasswordMenuItem.click()
      }
      
      KCreatingEntryScreen {
        iconBack {
          isDisplayed()
          click()
        }
      }
      
      recycler.isDisplayed()
      menu.isClosed()
      
      menu {
        open()
        newPasswordMenuItem.click()
      }
      
      KCreatingEntryScreen {
        title.isDisplayed()
        textWebsiteName.hasText("Website name")
        textLogin.hasText("Login")
        buttonContinue.isDisplayed()
        
        closeSoftKeyboard()
        buttonContinue.click()
        
        textWebsiteName {
          hasTextColorInt(Colors.Error)
          hasText("Website name cannot be empty")
        }
        textLogin {
          hasTextColorInt(Colors.Error)
          hasText("Login cannot be empty")
        }
        
        editTextWebsiteName.typeText("test.com")
        
        textWebsiteName {
          hasTextColorInt(Colors.Accent)
          hasText("Website name")
        }
        textLogin {
          hasTextColorInt(Colors.Error)
          hasText("Login cannot be empty")
        }
        
        editTextLogin.replaceText("mylogin")
        
        textLogin {
          hasTextColorInt(Colors.Accent)
          hasText("Login")
        }
        
        buttonContinue.click()
        
        KCreatingPasswordScreen {
          iconCross.click()
        }
        
        currentScreenIs(CreatingEntryScreen::class)
        
        buttonContinue.click()
        
        KCreatingPasswordScreen {
          title {
            isDisplayed()
            hasText("Password")
          }
          editTextPassword {
            isDisplayed()
            hasTextLength(16) // default password length
            hasPasswordWithAllCharacteristics()
          }
          textError.hasEmptyText()
          checkmarkUppercaseSymbols.isChecked()
          checkmarkNumbers.isChecked()
          checkmarkSpecialSymbols.isChecked()
          textPasswordLength.hasText("Length: 16")
          passwordLengthSpinner.hasProgress(8) // defaultPasswordLength - minPasswordLength
          buttonGeneratePassword.isDisplayed()
          buttonSavePassword.isDisplayed()
          
          passwordLengthSpinner.setProgress(13)
          
          buttonGeneratePassword.click()
          buttonGeneratePassword.click()
          buttonGeneratePassword.click()
          
          textPasswordLength.hasText("Length: 21")
          editTextPassword {
            hasTextLength(21)
            hasPasswordWithAllCharacteristics()
          }
          textPasswordStrength.hasText("Secure")
          passwordStrengthMeter.hasPasswordStrength(SECURE)
          
          checkmarkUppercaseSymbols.click()
          buttonGeneratePassword.click()
          
          checkmarkUppercaseSymbols.isNotChecked()
          checkmarkNumbers.isChecked()
          checkmarkSpecialSymbols.isChecked()
          editTextPassword {
            hasTextLength(21)
            hasPasswordWithCharacteristics(NUMBERS, SPECIAL_SYMBOLS)
          }
          
          checkmarkNumbers.click()
          buttonGeneratePassword.click()
          
          checkmarkUppercaseSymbols.isNotChecked()
          checkmarkNumbers.isNotChecked()
          checkmarkSpecialSymbols.isChecked()
          editTextPassword {
            hasTextLength(21)
            hasPasswordWithCharacteristics(SPECIAL_SYMBOLS)
          }
          
          checkmarkSpecialSymbols.click()
          buttonGeneratePassword.click()
          
          checkmarkUppercaseSymbols.isNotChecked()
          checkmarkNumbers.isNotChecked()
          checkmarkSpecialSymbols.isNotChecked()
          editTextPassword {
            hasTextLength(21)
            hasPasswordWithNoCharacteristics()
          }
          
          checkmarkUppercaseSymbols.click()
          checkmarkNumbers.click()
          checkmarkSpecialSymbols.click()
          buttonGeneratePassword.click()
  
          editTextPassword {
            hasTextLength(21)
            hasPasswordWithAllCharacteristics()
          }
  
          editTextPassword.replaceText("abcabcabcabcabc")
  
          checkmarkUppercaseSymbols.isNotChecked()
          checkmarkNumbers.isNotChecked()
          checkmarkUppercaseSymbols.isNotChecked()
          textPasswordStrength.hasText("Weak")
          passwordStrengthMeter.hasPasswordStrength(WEAK)
  
          editTextPassword.clearText()
          buttonSavePassword.click()
  
          textError.hasText("Password cannot be empty")
  
          buttonGeneratePassword.click()
          
          textError.hasEmptyText()
          
          buttonSavePassword.click()
          
          confirmationDialog {
            isDisplayed()
            title.hasText("Saving password")
            message.hasText("Do you want to accept the password?")
            action1.isNotDisplayed()
            action2.hasText("YES")
          }
          
          pressBack()
          
          confirmationDialog.isNotDisplayed()
          title.isDisplayed()
          
          buttonSavePassword.click()
          
          confirmationDialog.isDisplayed()
          
          confirmationDialog.hide()
          
          confirmationDialog.isNotDisplayed()
          
          buttonSavePassword.click()
          confirmationDialog.action2.click()
          
          KInfoScreen {
            imageWebsite {
              isDisplayed()
              hasDrawable(LetterInCircleDrawable("t"))
            }
            textWebsiteName.hasText("test.com")
            editTextWebsiteName.hasText("test.com")
            editTextLogin.hasText("mylogin")
            editTextNotes.hasEmptyText()
            
            pressBack()
          }
        }
      }
      
      recycler {
        isDisplayed()
        hasSize(1)
        firstChild<PasswordItem> {
          continuously {
            text.hasText("test.com")
            icon.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
      }
    }
  }
}
