package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KInfoScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.EmptyItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainListAndDeletingTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_items")
    rule.launchActivity()
  }
  
  @Test
  fun testMainListAndDeleting() = run {
    KLoginScreen {
      textError.hasEmptyText()
      editTextEnterPassword.hasHint("Enter password")
      
      editTextEnterPassword.replaceText("abc")
      buttonContinue.click()
      
      textError.hasText("Password is incorrect")
      currentScreenIs(LoginScreen::class)
      
      editTextEnterPassword.replaceText("qwetu1233")
      textError.hasEmptyText()
      buttonContinue.click()
      
      KMainListScreen {
        currentScreenIs(MainListScreen::class)
        recycler {
          isDisplayed()
          hasSize(2)
          childAt<PasswordItem>(0) {
            text.hasText("google")
            icon.hasDrawable(R.drawable.icon_google)
          }
          childAt<PasswordItem>(1) {
            text.hasText("test.com")
            icon.hasDrawable(LetterInCircleDrawable("t"))
          }
        }
        recycler.emptyFirstChild { click() }
        KInfoScreen {
          iconDelete.click()
          confirmationDialog.action2.click()
        }
        recycler {
          hasSize(1)
          firstChild<PasswordItem> {
            continuously {
              text.hasText("test.com")
              icon.hasDrawable(LetterInCircleDrawable("t"))
            }
          }
        }
        recycler.emptyFirstChild { click() }
        KInfoScreen {
          iconDelete.click()
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
