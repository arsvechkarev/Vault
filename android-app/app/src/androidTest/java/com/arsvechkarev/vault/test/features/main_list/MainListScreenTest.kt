package com.arsvechkarev.vault.test.features.main_list

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.test.core.VaultAutotestRule
import com.arsvechkarev.vault.test.core.ext.setUserLoggedIn
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.features.info.KInfoScreen
import com.arsvechkarev.vault.test.features.login.KLoginScreen
import com.arsvechkarev.vault.test.features.main_list.KMainListScreen.EmptyItem
import com.arsvechkarev.vault.test.features.main_list.KMainListScreen.PasswordItem
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainListScreenTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule(lazyLaunch = true)
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_items")
    setUserLoggedIn()
    rule.launchActivity()
  }
  
  @Test
  fun testMainListScreenAndDeletingFlow() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
    }
    KMainListScreen {
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
