package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.EmptyItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.TitleItem
import com.arsvechkarev.vault.test.screens.KPasswordEntryScreen
import org.junit.Rule
import org.junit.Test

class MainListTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Test
  fun testMainList() = init {
    rule.launchActivityWithDatabase("database_two_passwords")
  }.run {
    KLoginScreen {
      textError.hasEmptyText()
      editTextEnterPassword.hasHint("Enter password")
      
      editTextEnterPassword.replaceText("abc")
      buttonContinue.click()
      
      textError.hasText("Password is incorrect")
      currentScreenIs<LoginScreen>()
      
      editTextEnterPassword.replaceText("qwetu1233")
      textError.hasEmptyText()
      buttonContinue.click()
      
      KMainListScreen {
        currentScreenIs<MainListScreen>()
        recycler {
          isDisplayed()
          hasSize(3)
          childAt<TitleItem>(0) {
            title.hasText("Passwords")
          }
          childAt<PasswordItem>(1) {
            text.hasText("google")
            image.hasDrawable(R.drawable.icon_google)
          }
          childAt<PasswordItem>(2) {
            text.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        currentScreenIs<MainListScreen>()
        
        recycler {
          hasSize(2)
          childAt<PasswordItem>(1) {
            text.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        recycler {
          hasSize(1)
          firstChild<EmptyItem> {
            image.isDisplayed()
            title.isDisplayed()
            message.isDisplayed()
          }
        }
      }
    }
  }
}
