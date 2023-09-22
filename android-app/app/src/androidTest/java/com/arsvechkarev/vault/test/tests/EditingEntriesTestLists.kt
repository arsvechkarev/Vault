package com.arsvechkarev.vault.test.tests

import android.os.SystemClock.sleep
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.Durations
import com.arsvechkarev.vault.features.creating_password.CreatingPasswordScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.password_info.PasswordInfoScreen
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen
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

class EditingEntriesTestLists : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_two_passwords_and_plain_text")
    rule.launchActivity()
  }
  
  @Test
  fun testEditingPasswordInfo() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler.emptyChildAt(1) { click() }
        KPasswordInfoScreen {
          iconBack.isDisplayed()
          imageTitle {
            isDisplayed()
            hasDrawable(R.drawable.icon_google)
          }
          textTitle.hasText("google")
          titleTitle.hasText("Title")
          editTextTitle.hasText("google")
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          titleUsername.hasText("Username")
          editTextUsername.hasText("me@gmail.com")
          imageUsernameAction.hasDrawable(R.drawable.ic_copy)
          titlePassword.hasText("Password")
          textHiddenPassword.hasText(R.string.text_password_stub)
          titleNotes.hasText("Notes")
          editTextNotes.hasEmptyText()
          imageNotesAction.hasDrawable(R.drawable.ic_copy)
          snackbar.isNotDisplayed()
          
          iconBack.click()
          
          KMainListScreen {
            currentScreenIs<MainListScreen>()
            recycler.emptyChildAt(1) { click() }
          }
          
          currentScreenIs<PasswordInfoScreen>()
          
          imageTitleAction.click()
          hasClipboardText("google")
          snackbar.isDisplayedWithText("Title successfully copied!")
          
          waitForSnackbarToHide()
          
          imageUsernameAction.click()
          hasClipboardText("me@gmail.com")
          snackbar.isDisplayedWithText("Username successfully copied!")
          
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
          
          currentScreenIs<PasswordInfoScreen>()
          
          imageNotesAction.hasDrawable(R.drawable.ic_copy)
          editTextNotes.hasEmptyText()
          
          editTextNotes.replaceText("my notes2")
          
          imageNotesAction.click()
          
          editTextNotes.hasText("my notes2")
          
          imageNotesAction.click()
          
          hasClipboardText("my notes2")
          snackbar.isDisplayedWithText("Notes successfully copied!")
          
          waitForSnackbarToHide()
          
          editTextTitle.typeText("a")
          
          imageTitleAction.hasDrawable(R.drawable.ic_checmark)
          editTextTitle.hasText("googlea")
          textTitle.hasText("googlea")
          imageTitle.hasDrawable(R.drawable.icon_google)
          
          iconBack.click()
          
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          editTextTitle.hasText("google")
          textTitle.hasText("google")
          
          editTextTitle.replaceText("yabcd")
          imageTitleAction.click()
          
          imageTitle.hasDrawable(LetterInCircleDrawable("y"))
          textTitle.hasText("yabcd")
          editTextTitle.hasText("yabcd")
          
          imageTitleAction.click()
          
          hasClipboardText("yabcd")
          snackbar.isDisplayedWithText("Title successfully copied!")
          
          iconBack.click()
          
          KMainListScreen {
            currentScreenIs<MainListScreen>()
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
          
          currentScreenIs<PasswordInfoScreen>()
          
          editTextUsername.replaceText("qwerty")
          
          imageUsernameAction.hasDrawable(R.drawable.ic_checmark)
          
          pressBack()
          
          imageUsernameAction.hasDrawable(R.drawable.ic_copy)
          editTextUsername.hasText("me@gmail.com")
          
          editTextUsername.replaceText("kkk")
          
          imageUsernameAction.click()
          
          imageUsernameAction.hasDrawable(R.drawable.ic_copy)
          editTextUsername.hasText("kkk")
          
          imageUsernameAction.click()
          
          hasClipboardText("kkk")
          snackbar.isDisplayedWithText("Username successfully copied!")
          
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            currentScreenIs<CreatingPasswordScreen>()
            editTextPassword.hasText("F/<1#E(J=\\51=k")
            title.hasText("Edit password")
            
            pressBack()
          }
          
          currentScreenIs<PasswordInfoScreen>()
          
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.clearText()
            iconCross.click()
          }
          
          currentScreenIs<PasswordInfoScreen>()
          
          imageEditPassword.click()
          
          KCreatingPasswordScreen {
            editTextPassword.replaceText("qwerty222;")
            
            buttonSavePassword.click()
            
            confirmationDialog.isDisplayed()
            
            confirmationDialog.action2.click()
          }
          
          currentScreenIs<PasswordInfoScreen>()
          
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
          
          currentScreenIs<PlainTextScreen>()
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
          
          currentScreenIs<PlainTextScreen>()
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
        
        currentScreenIs<MainListScreen>()
        
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
        
        currentScreenIs<MainListScreen>()
        
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
