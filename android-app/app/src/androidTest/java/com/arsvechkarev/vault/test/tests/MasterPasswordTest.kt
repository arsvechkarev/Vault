package com.arsvechkarev.vault.test.tests

import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.initial.InitialScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KSettingsScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class MasterPasswordTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Test
  fun testCreatingNewMasterPassword() = init {
    rule.launchActivity()
  }.run {
    KInitialScreen {
      buttonCreateMasterPassword.click()
    }
    KMasterPasswordScreen {
      closeSoftKeyboard()
      pressBack()
    }
    
    currentScreenIs<InitialScreen>()
    
    KInitialScreen {
      buttonCreateMasterPassword.click()
    }
    
    KMasterPasswordScreen {
      title.hasText("Create master password")
      buttonContinue.isCompletelyDisplayed()
      textPasswordStrength.isNotDisplayed()
      passwordStrengthMeter.isNotShowingStrength()
      editTextEnterPassword {
        isDisplayed()
        hasEmptyText()
        isPasswordHidden()
        hasHint("Enter password")
      }
      
      buttonContinue.click()
      
      textError {
        isDisplayed()
        hasText("Password is empty")
      }
      iconError.isNotDisplayed()
      textPasswordStrength.isNotDisplayed()
      passwordStrengthMeter.isNotShowingStrength()
      passwordStrengthDialog.isNotShown()
      
      editTextEnterPassword.typeText("aa")
      
      textError.isNotDisplayed()
      
      editTextEnterPassword.toggleVisibility()
      editTextEnterPassword.isPasswordVisible()
      
      buttonContinue.click()
      
      textError {
        isDisplayed()
        hasText("Password is too weak")
      }
      iconError.isDisplayed()
      textPasswordStrength.hasText("Weak")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.WEAK)
      
      iconError.click()
      
      passwordStrengthDialog.isShown()
      
      closeSoftKeyboard()
      pressBack()
      
      passwordStrengthDialog.isNotShown()
      
      iconError.click()
      
      passwordStrengthDialog.proceedWithWeakPassword()
      
      textError.isNotDisplayed()
      iconError.isNotDisplayed()
      repeatPasswordLayout.isDisplayed()
      
      pressBack()
      
      repeatPasswordLayout.isNotDisplayed()
      
      editTextEnterPassword.replaceText("qwetu123")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.MEDIUM)
      textPasswordStrength.hasText("Medium")
      
      editTextEnterPassword.typeText("/")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.STRONG)
      textPasswordStrength.hasText("Strong")
      
      editTextEnterPassword.typeText("yd2")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.SECURE)
      textPasswordStrength.hasText("Secure")
      
      buttonContinue.click()
      
      repeatPasswordTitle.isDisplayed()
      repeatPasswordLayout.isDisplayed()
      title.isNotDisplayed()
      
      editTextEnterPassword.isNotDisplayed()
      imageBack.isDisplayed()
      editTextRepeatPassword {
        isDisplayed()
        hasEmptyText()
        isPasswordHidden()
        hasHint("Repeat password")
      }
      
      editTextRepeatPassword.replaceText("ppp")
      buttonContinue.click()
      
      textError {
        isDisplayed()
        hasText("Passwords don't match")
      }
      iconError.isNotDisplayed()
      
      editTextRepeatPassword.typeText("o")
      
      textError.isNotDisplayed()
      
      imageBack.click()
      
      editTextRepeatPassword.isNotDisplayed()
      title.isDisplayed()
      editTextEnterPassword {
        isDisplayed()
        isPasswordVisible()
        hasText("qwetu123/yd2")
      }
      
      buttonContinue.click()
      
      editTextRepeatPassword.replaceText("qwetu123/yd2")
      
      buttonContinue.click()
    }
    currentScreenIs<MainListScreen>()
  }
  
  @Test
  fun testChangeExistingMasterPassword() = init {
    rule.launchActivityWithDatabase("database_two_passwords")
  }.run {
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
          
          enterPasswordDialog {
            isDisplayed()
            title.hasText("Enter master password to continue")
            textError.hasEmptyText()
            
            editText.replaceText("abc")
            buttonContinue.click()
            
            textError.hasText("Password is incorrect")
            
            editText.replaceText("qwetu1233")
            
            textError.hasEmptyText()
            
            buttonContinue.click()
            
            KMasterPasswordScreen {
              editTextEnterPassword.replaceText("qwetu1233")
              
              buttonContinue.click()
              
              textError {
                hasText("Password is the same as current one")
                isDisplayed()
              }
              iconError.isNotDisplayed()
              
              editTextEnterPassword.replaceText("abcd90-+321")
              
              buttonContinue.click()
              
              editTextRepeatPassword.replaceText("abcd90-+321")
              
              buttonContinue.click()
              
              confirmationDialog {
                isDisplayed()
                title.hasText("Confirmation")
                message.hasText(R.string.text_confirmation_message)
                action1.hasText("CANCEL")
                action2.hasText("CONTINUE")
              }
              
              closeSoftKeyboard()
              pressBack()
              
              confirmationDialog.isNotDisplayed()
              
              buttonContinue.click()
              
              confirmationDialog.isDisplayed()
              
              confirmationDialog.action1.click()
              
              confirmationDialog.isNotDisplayed()
              
              buttonContinue.click()
              
              confirmationDialog.action2.click()
              
              loadingDialog.isDisplayed()
            }
          }
          
          currentScreenIs<SettingsScreen>()
          
          snackbar.isDisplayedWithText("Master password successfully changed")
        }
      }
    }
    
    rule.finishActivity()
    
    rule.launchActivity()
    
    KLoginScreen {
      editTextEnterPassword.clearText()
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      textError.hasText("Password is incorrect")
      
      editTextEnterPassword.replaceText("abcd90-+321")
      buttonContinue.click()
      
      KMainListScreen {
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            text.hasText("google")
            image.hasDrawable(R.drawable.icon_google)
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
