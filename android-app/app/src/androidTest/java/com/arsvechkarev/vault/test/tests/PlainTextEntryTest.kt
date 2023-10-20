package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasClipboardText
import com.arsvechkarev.vault.test.core.ext.hasTextColorInt
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.ext.waitForSnackbarToHide
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PlainTextItem
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import com.arsvechkarev.vault.viewbuilding.Colors
import org.junit.Rule
import org.junit.Test

class PlainTextEntryTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Test
  fun testCreatingPlainText() = init {
    rule.launchActivityWithDatabase("database_empty")
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
    }
    KMainListScreen {
      menu {
        open()
        newEntryMenuItem.click()
      }
      entryTypeDialog.plainTextEntry.click()
      
      KPlainTextEntryScreen {
        imageBack.click()
      }
      
      currentScreenIs<MainListScreen>()
      
      menu {
        open()
        newEntryMenuItem.click()
      }
      entryTypeDialog.plainTextEntry.click()
      
      KPlainTextEntryScreen {
        textMainTitle.hasText("New plain text")
        imageDelete.isNotDisplayed()
        imageTitleAction.isNotDisplayed()
        imageTextAction.isNotDisplayed()
        title.hasText("Title")
        editTextTitle.hasEmptyText()
        titleText.hasText("Text")
        editTextText.hasEmptyText()
        buttonSave.isDisplayed()
        
        buttonSave.click()
        
        title.hasText("Title is empty")
        title.hasTextColorInt(Colors.TextError)
        
        editTextTitle.typeText("abc")
        
        title.hasText("Title")
        title.hasTextColorInt(Colors.Accent)
        
        editTextText.typeText("this is text")
        
        buttonSave.click()
        
        snackbar.isDisplayedWithText("Plain text entry created")
        
        pressBack()
      }
      
      recycler {
        hasSize(2)
        childAt<PlainTextItem>(1) {
          title.hasText("abc")
        }
      }
    }
  }
  
  @Test
  fun testEditingPlainText() = init {
    rule.launchActivityWithDatabase("database_two_passwords_and_plain_text")
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler.emptyChildAt(4) { click() }
        
        KPlainTextEntryScreen {
          textMainTitle.hasText("Plain text")
          imageDelete.isDisplayed()
          title.hasText("Title")
          editTextTitle.hasText("my title")
          imageTitleAction.isDisplayed()
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          titleText.hasText("Text")
          editTextText.hasText("super secret content")
          imageTextAction.isDisplayed()
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          buttonSave.isNotDisplayed()
          
          imageTitleAction.click()
          hasClipboardText("my title")
          snackbar.isDisplayedWithText("Title successfully copied")
          
          waitForSnackbarToHide()
          
          imageTextAction.click()
          hasClipboardText("super secret content")
          snackbar.isDisplayedWithText("Text successfully copied")
          
          waitForSnackbarToHide()
          
          editTextText.replaceText("description2")
          
          imageTextAction.hasDrawable(R.drawable.ic_checmark)
          
          pressBack()
          
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          editTextText.hasText("super secret content")
          
          editTextText.clearText()
          
          imageTextAction.click()
          
          editTextText.hasEmptyText()
          imageTextAction.hasDrawable(R.drawable.ic_copy)
          
          imageTextAction.click()
          
          hasClipboardText("")
          snackbar.isDisplayedWithText("Text successfully copied")
          
          waitForSnackbarToHide()
          
          editTextTitle.clearText()
          
          imageTitleAction.click()
          
          imageTitleAction.hasDrawable(R.drawable.ic_checmark)
          title.hasText("Title is empty")
          title.hasTextColorInt(Colors.TextError)
          
          editTextTitle.replaceText("a")
          
          title.hasText("Title")
          title.hasTextColorInt(Colors.Accent)
          
          imageBack.click()
          
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          editTextTitle.hasText("my title")
          
          editTextTitle.typeText("2")
          
          imageTitleAction.click()
          
          editTextTitle.hasText("my title2")
          imageTitleAction.hasDrawable(R.drawable.ic_copy)
          
          imageTitleAction.click()
          
          hasClipboardText("my title2")
          snackbar.isDisplayedWithText("Title successfully copied")
          
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
            title.hasText("Delete plain text")
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
}