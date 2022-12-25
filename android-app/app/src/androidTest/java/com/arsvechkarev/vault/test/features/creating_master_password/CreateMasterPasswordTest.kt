package com.arsvechkarev.vault.test.features.creating_master_password

import buisnesslogic.PasswordStrength
import com.arsvechkarev.vault.test.common.VaultAutotestRule
import com.arsvechkarev.vault.test.features.initial.KInitialScreen
import com.arsvechkarev.vault.test.features.main_list.KMainListScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class CreateMasterPasswordTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Test
  fun testCreateMasterPasswordFlow() = run {
    KInitialScreen {
      buttonCreateMasterPassword.click()
    }
    KCreatingMasterPasswordScreen {
      closeSoftKeyboard()
      pressBack()
    }
    KInitialScreen {
      buttonCreateMasterPassword {
        isVisible()
        click()
      }
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
      
      editTextRepeatPassword.typeText("od")
      
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
    KMainListScreen {
      title.isDisplayed()
    }
  }
}
