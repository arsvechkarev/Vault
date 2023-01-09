package com.arsvechkarev.vault.test.tests

import android.os.SystemClock.sleep
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.info.InfoScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasClipboardText
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KInfoScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditingInfoAndPasswordTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_items")
    rule.launchActivity()
  }
  
  @Test
  fun testEditingInfoAndPassword() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler.emptyFirstChild { click() }
        KInfoScreen {
          iconBack.isDisplayed()
          imageWebsite {
            isDisplayed()
            hasDrawable(R.drawable.icon_google)
          }
          textWebsiteName.hasText("google")
          titleWebsiteName.hasText("Website name")
          editTextWebsiteName.hasText("google")
          buttonWebsiteNameAction.hasDrawable(R.drawable.ic_copy)
          titleLogin.hasText("Login")
          editTextLogin.hasText("me@gmail.com")
          buttonLoginAction.hasDrawable(R.drawable.ic_copy)
          titlePassword.hasText("Password")
          textHiddenPassword.hasText(R.string.text_password_stub)
          titleNotes.hasText("Notes")
          editTextNotes.hasEmptyText()
          buttonNotesAction.hasDrawable(R.drawable.ic_copy)
          snackbar.isNotDisplayed()
          
          iconBack.click()
          
          KMainListScreen {
            currentScreenIs(MainListScreen::class)
            recycler.emptyFirstChild { click() }
          }
          
          currentScreenIs(InfoScreen::class)
          
          buttonWebsiteNameAction.click()
          hasClipboardText("google")
          snackbar.isDisplayedWithText("Website successfully copied!")
          
          waitForSnackbarToHide()
          
          buttonLoginAction.click()
          hasClipboardText("me@gmail.com")
          snackbar.isDisplayedWithText("Login successfully copied!")
          
          waitForSnackbarToHide()
          
          buttonCopyPassword.click()
          hasClipboardText("F/<1#E(J=\\51=k")
          snackbar.isDisplayedWithText("Password successfully copied!")
          
          waitForSnackbarToHide()
          
          buttonNotesAction.click()
          hasClipboardText("")
          snackbar.isDisplayedWithText("Notes successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextNotes.replaceText("my notes")
          
          buttonNotesAction.hasDrawable(R.drawable.ic_checmark)
          
          iconBack.click()
          
          currentScreenIs(InfoScreen::class)
          buttonNotesAction.hasDrawable(R.drawable.ic_copy)
          editTextNotes.hasEmptyText()
          
          editTextNotes.replaceText("my notes2")
          
          buttonNotesAction.click()
          
          editTextNotes.hasText("my notes2")
          
          buttonNotesAction.click()
          
          hasClipboardText("my notes2")
          snackbar.isDisplayedWithText("Notes successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextWebsiteName.typeText("a")
          
          buttonWebsiteNameAction.hasDrawable(R.drawable.ic_checmark)
          editTextWebsiteName.hasText("googlea")
          textWebsiteName.hasText("googlea")
          imageWebsite.hasDrawable(R.drawable.icon_google)
          
          iconBack.click()
          
          buttonWebsiteNameAction.hasDrawable(R.drawable.ic_copy)
          editTextWebsiteName.hasText("google")
          textWebsiteName.hasText("google")
          
          editTextWebsiteName.replaceText("yabcd")
          buttonWebsiteNameAction.click()
          
          imageWebsite.hasDrawable(LetterInCircleDrawable("y"))
          textWebsiteName.hasText("yabcd")
          editTextWebsiteName.hasText("yabcd")
          
          buttonWebsiteNameAction.click()
          
          hasClipboardText("yabcd")
          snackbar.isDisplayedWithText("Website successfully copied!")
          
          iconBack.click()
          
          KMainListScreen {
            recycler {
              isDisplayed()
              hasSize(2)
              childAt<PasswordItem>(0) {
                text.hasText("test.com")
                icon.hasDrawable(LetterInCircleDrawable("t"))
              }
              childAt<PasswordItem>(1) {
                text.hasText("yabcd")
                icon.hasDrawable(LetterInCircleDrawable("y"))
              }
            }
            
            recycler.emptyChildAt(1) { click() }
          }
          
          editTextLogin.replaceText("qwerty")
          
          buttonLoginAction.hasDrawable(R.drawable.ic_checmark)
          
          pressBack()
          
          buttonLoginAction.hasDrawable(R.drawable.ic_copy)
          editTextLogin.hasText("me@gmail.com")
          
          editTextLogin.replaceText("kkk")
          
          buttonLoginAction.click()
          
          buttonLoginAction.hasDrawable(R.drawable.ic_copy)
          editTextLogin.hasText("kkk")
          
          buttonLoginAction.click()
          
          hasClipboardText("kkk")
          snackbar.isDisplayedWithText("Login successfully copied!")
          
          buttonEditPassword.click()
          
          KCreatingPasswordScreen {
            currentScreenIs(CreatingPasswordScreen::class)
            editTextPassword.hasText("F/<1#E(J=\\51=k")
            title.hasText("Edit password")
            
            pressBack()
          }
          
          currentScreenIs(InfoScreen::class)
          
          buttonEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.clearText()
            iconCross.click()
          }
          
          currentScreenIs(InfoScreen::class)
          
          buttonEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.replaceText("qwerty222;")
            
            buttonSavePassword.click()
            
            confirmationDialog.isDisplayed()
            
            confirmationDialog.action2.click()
          }
          
          currentScreenIs(InfoScreen::class)
          
          buttonCopyPassword.click()
          hasClipboardText("qwerty222;")
          
          buttonEditPassword.click()
          
          KCreatingPasswordScreen {
            checkmarkUppercaseSymbols.isNotChecked()
            checkmarkNumbers.isChecked()
            checkmarkSpecialSymbols.isChecked()
          }
        }
      }
    }
  }
  
  private fun waitForSnackbarToHide() {
    sleep(Durations.Snackbar * 3)
  }
}
