package com.arsvechkarev.vault.test.tests

import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.features.initial.InitialScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.VaultAutotestRule
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.screens.KCreatingMasterPasswordScreen
import com.arsvechkarev.vault.test.screens.KInitialScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class CreatingMasterPasswordTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule(autoLaunch = true)
  
  @Test
  fun testCreatingMasterPassword() = run {
    KInitialScreen {
      buttonCreateMasterPassword.click()
    }
    KCreatingMasterPasswordScreen {
      closeSoftKeyboard()
      pressBack()
    }
    
    currentScreenIs(InitialScreen::class)
    
    KInitialScreen {
      buttonCreateMasterPassword.click()
    }
    
    KCreatingMasterPasswordScreen {
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
        hasText("Password cannot be empty")
      }
      textPasswordStrength.isNotDisplayed()
      passwordStrengthMeter.isNotShowingStrength()
      passwordStrengthDialog.isNotShown()
      
      editTextEnterPassword.typeText("aa")
      
      textError.isNotDisplayed()
      
      editTextEnterPassword.toggleVisibility()
      editTextEnterPassword.isPasswordVisible()
      
      buttonContinue.click()
      
      title.hasText("Create master password")
      textError {
        isDisplayed()
        hasText("Password should be at least 8 characters long")
      }
      textPasswordStrength.hasText("Weak")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.WEAK)
      
      editTextEnterPassword.replaceText("aaaaaaaa")
      buttonContinue.click()
      
      textPasswordStrength.hasText("Weak")
      passwordStrengthMeter.hasPasswordStrength(PasswordStrength.WEAK)
      textError.hasText("Password is too weak")
      iconError.isVisible()
      
      iconError.click()
      
      passwordStrengthDialog.isShown()
      
      passwordStrengthDialog.hide()
      
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
      iconBack.isDisplayed()
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
      
      editTextRepeatPassword.typeText("o")
      
      textError.isNotDisplayed()
      
      iconBack.click()
      
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
    currentScreenIs(MainListScreen::class)
  }
}
