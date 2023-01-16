package com.arsvechkarev.vault.test.tests

import android.os.SystemClock.sleep
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen
import com.arsvechkarev.vault.features.plain_text_entry.PlainTextScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasClipboardText
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KCreatingPasswordScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PlainTextItem
import com.arsvechkarev.vault.test.screens.KPasswordInfoScreen
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class EditingEntriesTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_passwords_and_plain_text")
    rule.launchActivity()
  }
  
  @Test
  fun testEditingInfoAndPassword() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler.emptyChildAt(1) { click() }
        KPasswordInfoScreen {
          iconBack.isDisplayed()
          imageWebsite {
            isDisplayed()
            hasDrawable(R.drawable.icon_google)
          }
          textWebsiteName.hasText("google")
          titleWebsiteName.hasText("Website name")
          editTextWebsiteName.hasText("google")
          imageWebsiteNameAction.hasDrawable(R.drawable.ic_copy)
          titleLogin.hasText("Login")
          editTextLogin.hasText("me@gmail.com")
          imageLoginAction.hasDrawable(R.drawable.ic_copy)
          titlePassword.hasText("Password")
          textHiddenPassword.hasText(R.string.text_password_stub)
          titleNotes.hasText("Notes")
          editTextNotes.hasEmptyText()
          imageNotesAction.hasDrawable(R.drawable.ic_copy)
          snackbar.isNotDisplayed()
          
          iconBack.click()
          
          KMainListScreen {
            currentScreenIs(MainListScreen::class)
            recycler.emptyChildAt(1) { click() }
          }
  
          currentScreenIs(PasswordInfoScreen::class)
  
          imageWebsiteNameAction.click()
          hasClipboardText("google")
          snackbar.isDisplayedWithText("Website successfully copied!")
          
          waitForSnackbarToHide()
  
          imageLoginAction.click()
          hasClipboardText("me@gmail.com")
          snackbar.isDisplayedWithText("Login successfully copied!")
          
          waitForSnackbarToHide()
  
          imageCopyPassword.click()
          hasClipboardText("F/<1#E(J=\\51=k")
          snackbar.isDisplayedWithText("Password successfully copied!")
          
          waitForSnackbarToHide()
  
          imageNotesAction.click()
          hasClipboardText("")
          snackbar.isDisplayedWithText("Notes successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextNotes.replaceText("my notes")
  
          imageNotesAction.hasDrawable(R.drawable.ic_checmark)
          
          iconBack.click()
  
          currentScreenIs(PasswordInfoScreen::class)
          imageNotesAction.hasDrawable(R.drawable.ic_copy)
          editTextNotes.hasEmptyText()
          
          editTextNotes.replaceText("my notes2")
  
          imageNotesAction.click()
          
          editTextNotes.hasText("my notes2")
  
          imageNotesAction.click()
          
          hasClipboardText("my notes2")
          snackbar.isDisplayedWithText("Notes successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextWebsiteName.typeText("a")
  
          imageWebsiteNameAction.hasDrawable(R.drawable.ic_checmark)
          editTextWebsiteName.hasText("googlea")
          textWebsiteName.hasText("googlea")
          imageWebsite.hasDrawable(R.drawable.icon_google)
          
          iconBack.click()
  
          imageWebsiteNameAction.hasDrawable(R.drawable.ic_copy)
          editTextWebsiteName.hasText("google")
          textWebsiteName.hasText("google")
  
          editTextWebsiteName.replaceText("yabcd")
          imageWebsiteNameAction.click()
          
          imageWebsite.hasDrawable(LetterInCircleDrawable("y"))
          textWebsiteName.hasText("yabcd")
          editTextWebsiteName.hasText("yabcd")
  
          imageWebsiteNameAction.click()
          
          hasClipboardText("yabcd")
          snackbar.isDisplayedWithText("Website successfully copied!")
          
          iconBack.click()
          
          KMainListScreen {
            recycler {
              isDisplayed()
              hasSize(5)
              childAt<PasswordItem>(1) {
                text.hasText("test.com")
                icon.hasDrawable(LetterInCircleDrawable("t"))
              }
              childAt<PasswordItem>(2) {
                text.hasText("yabcd")
                icon.hasDrawable(LetterInCircleDrawable("y"))
              }
              childAt<PlainTextItem>(4) {
                title.hasText("my title")
              }
            }
  
            recycler.emptyChildAt(2) { click() }
          }
          
          editTextLogin.replaceText("qwerty")
  
          imageLoginAction.hasDrawable(R.drawable.ic_checmark)
          
          pressBack()
  
          imageLoginAction.hasDrawable(R.drawable.ic_copy)
          editTextLogin.hasText("me@gmail.com")
          
          editTextLogin.replaceText("kkk")
  
          imageLoginAction.click()
  
          imageLoginAction.hasDrawable(R.drawable.ic_copy)
          editTextLogin.hasText("kkk")
  
          imageLoginAction.click()
          
          hasClipboardText("kkk")
          snackbar.isDisplayedWithText("Login successfully copied!")
  
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            currentScreenIs(CreatingPasswordScreen::class)
            editTextPassword.hasText("F/<1#E(J=\\51=k")
            title.hasText("Edit password")
            
            pressBack()
          }
  
          currentScreenIs(PasswordInfoScreen::class)
  
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.clearText()
            iconCross.click()
          }
  
          currentScreenIs(PasswordInfoScreen::class)
  
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.replaceText("qwerty222;")
            
            buttonSavePassword.click()
            
            confirmationDialog.isDisplayed()
            
            confirmationDialog.action2.click()
          }
  
          currentScreenIs(PasswordInfoScreen::class)
  
          imageCopyPassword.click()
          hasClipboardText("qwerty222;")
  
          imageEditPassword.click()
  
          KCreatingPasswordScreen {
            checkmarkUppercaseSymbols.isNotChecked()
            checkmarkNumbers.isChecked()
            checkmarkSpecialSymbols.isChecked()
          }
        }
      }
    }
  }
  
  @Test
  fun testEditingPlainText() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler.emptyChildAt(4) { click() }
        
        KPlainTextEntryScreen {
          textMainTitle.hasText("Plain text")
          imageDelete.isDisplayed()
          textTitle.hasText("Title")
          editTextTitle.hasText("my title")
          imageTitleAction.isDisplayed()
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          textText.hasText("Text")
          editTextText.hasText("super secret content")
          imageTextAction.isDisplayed()
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          buttonSave.isNotDisplayed()
          
          imageTitleAction.click()
          hasClipboardText("my title")
          snackbar.isDisplayedWithText("Title successfully copied!")
          
          waitForSnackbarToHide()
          
          imageTextAction.click()
          hasClipboardText("super secret content")
          snackbar.isDisplayedWithText("Text successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextText.replaceText("description2")
          
          imageTextAction.hasDrawable(R.drawable.ic_checmark)
          
          pressBack()
          
          currentScreenIs(PlainTextScreen::class)
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          editTextText.hasText("super secret content")
          
          editTextText.replaceText("")
          
          imageTextAction.click()
          
          editTextText.hasEmptyText()
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          
          imageTextAction.click()
          
          hasClipboardText("")
          snackbar.isDisplayedWithText("Text successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextTitle.replaceText("")
          
          imageTitleAction.hasDrawable(R.drawable.ic_checmark)
          
          imageTitleAction.click()
          
          imageTitleAction.hasDrawable(R.drawable.ic_checmark)
          
          iconBack.click()
          
          currentScreenIs(PlainTextScreen::class)
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          editTextTitle.hasText("my title")
          
          editTextTitle.typeText("2")
          
          imageTitleAction.click()
          
          editTextTitle.hasText("my title2")
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          
          imageTitleAction.click()
          
          hasClipboardText("my title2")
          snackbar.isDisplayedWithText("Title successfully copied!")
          
          pressBack()
        }
        
        recycler {
          childAt<PlainTextItem>(4) {
            title.hasText("my title2")
            click()
          }
        }
        
        KPlainTextEntryScreen {
          editTextTitle.hasText("my title2")
          imageDelete.click()
          
          confirmationDialog {
            isDisplayed()
            title.hasText("Deleting entry")
            message.hasText("Do you want to delete my title2?")
            action1.hasText("CANCEL")
            action2.hasText("DELETE")
          }
          
          confirmationDialog.hide()
          
          confirmationDialog.isNotDisplayed()
          
          imageDelete.click()
          
          confirmationDialog.action2.click()
        }
        
        currentScreenIs(MainListScreen::class)
        recycler {
          hasSize(3)
          childAt<PasswordItem>(1) {
            text.hasText("google")
          }
          childAt<PasswordItem>(2) {
            text.hasText("test.com")
          }
        }
      }
    }
  }
  
  private fun waitForSnackbarToHide() {
    sleep(Durations.Snackbar * 3)
  }
}
