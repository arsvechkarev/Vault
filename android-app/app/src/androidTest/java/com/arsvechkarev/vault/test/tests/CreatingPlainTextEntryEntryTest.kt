package com.arsvechkarev.vault.test.tests

import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.plain_text_info.PlainTextScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PlainTextItem
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreatingPlainTextEntryEntryTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  @Before
  fun setup() = runBlocking {
    writeVaultFileFromAssets("file_no_items")
    rule.launchActivity()
  }
  
  
  @Test
  fun testCreatingPlainText() = run {
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
        iconBack.click()
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
        textTitle.hasText("Title")
        editTextTitle.hasEmptyText()
        textText.hasText("Text")
        editTextText.hasEmptyText()
        buttonSave.isDisplayed()
        
        buttonSave.click()
        
        textTitle.hasText("Title cannot be empty")
        
        editTextTitle.typeText("abc")
        
        textTitle.hasText("Title")
        
        editTextText.typeText("this is text")
        
        buttonSave.click()
        
        confirmationDialog {
          isDisplayed()
          title.hasText("Confirmation")
          message.hasText("Do you want to save the data?")
          action2.hasText("YES")
        }
        
        pressBack()
        
        currentScreenIs<PlainTextScreen>()
        
        confirmationDialog.isNotDisplayed()
        
        buttonSave.click()
        confirmationDialog.action2.click()
        
        snackbar.isDisplayedWithText("Plain text entry created!")
        
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
}
