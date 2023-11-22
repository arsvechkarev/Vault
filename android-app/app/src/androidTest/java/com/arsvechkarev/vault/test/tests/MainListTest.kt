package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.login.LoginScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.TestImageRequestsRecorder
import com.arsvechkarev.vault.test.core.di.stubs.URL_IMAGE_GOOGLE
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.ext.wasImageRequestWithUrlCalled
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KImportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.EmptyItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.EmptySearchItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.PlainTextItem
import com.arsvechkarev.vault.test.screens.KMainListScreen.TitleItem
import com.arsvechkarev.vault.test.screens.KPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KPlainTextEntryScreen
import com.arsvechkarev.vault.test.screens.KSettingsScreen
import org.junit.Rule
import org.junit.Test

class MainListTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val imageRequestsRecorder = TestImageRequestsRecorder()
  
  @Test
  fun testMainList() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        imagesRequestsRecorder = imageRequestsRecorder
      )
    )
    rule.launchActivityWithDatabase("file_two_passwords_and_plain_text")
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
        imageSearchAction.isDisplayed()
        recycler {
          isDisplayed()
          hasSize(5)
          childAt<TitleItem>(0) {
            title.hasText("Passwords")
          }
          childAt<PasswordItem>(1) {
            title.hasText("google")
            image.wasImageRequestWithUrlCalled(URL_IMAGE_GOOGLE, imageRequestsRecorder)
            subtitle.isNotDisplayed()
          }
          childAt<PasswordItem>(2) {
            title.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
          childAt<TitleItem>(3) {
            title.hasText("Plain texts")
          }
          childAt<PlainTextItem>(4) {
            title.hasText("my title")
          }
        }
        
        menu {
          open()
          settingsMenuItem.click()
        }
        
        KSettingsScreen {
          switchShowUsernames.isNotChecked()
          switchShowUsernames.click()
          pressBack()
        }
        
        recycler {
          childAt<PasswordItem>(1) {
            title.hasText("google")
            subtitle {
              isDisplayed()
              hasText("me@gmail.com")
            }
          }
          childAt<PasswordItem>(2) {
            title.hasText("test.com")
            subtitle {
              isDisplayed()
              hasText("abcd")
            }
          }
        }
        
        menu {
          open()
          settingsMenuItem.click()
        }
        
        KSettingsScreen {
          switchShowUsernames.isChecked()
          switchShowUsernames.click()
          pressBack()
        }
        
        recycler {
          childAt<PasswordItem>(1) {
            subtitle.isNotDisplayed()
          }
          childAt<PasswordItem>(2) {
            subtitle.isNotDisplayed()
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          imageFavorite.click()
          pressBack()
        }
        
        recycler {
          hasSize(6)
          childAt<TitleItem>(0) {
            title.hasText("Favorites")
          }
          childAt<PasswordItem>(1) {
            title.hasText("google")
            image.wasImageRequestWithUrlCalled(URL_IMAGE_GOOGLE, imageRequestsRecorder)
          }
          childAt<TitleItem>(2) {
            title.hasText("Passwords")
          }
          childAt<PasswordItem>(3) {
            title.hasText("test.com")
            image.hasDrawable(LetterInCircleDrawable("t"))
          }
          childAt<TitleItem>(4) {
            title.hasText("Plain texts")
          }
          childAt<PlainTextItem>(5) {
            title.hasText("my title")
          }
        }
        
        recycler.emptyChildAt(3) { click() }
        
        KPasswordEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        currentScreenIs<MainListScreen>()
        
        recycler {
          hasSize(4)
          childAt<TitleItem>(0) {
            title.hasText("Favorites")
          }
          childAt<PasswordItem>(1) {
            title.hasText("google")
          }
          childAt<TitleItem>(2) {
            title.hasText("Plain texts")
          }
          childAt<PlainTextItem>(3) {
            title.hasText("my title")
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        recycler {
          hasSize(2)
          childAt<TitleItem>(0) {
            title.hasText("Plain texts")
          }
          childAt<PlainTextItem>(1) {
            title.hasText("my title")
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPlainTextEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        imageSearchAction.isNotDisplayed()
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
  
  @Test
  fun testSearch() = init {
    rule.launchActivityWithDatabase("file_two_passwords_and_plain_text")
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        editTextSearch.isNotDisplayed()
        imageSearchAction.hasDrawable(R.drawable.ic_search)
        
        imageSearchAction.click()
        
        imageSearchAction.hasDrawable(R.drawable.ic_cross)
        title.isNotDisplayed()
        editTextSearch {
          isDisplayed()
          hasHint("Search…")
        }
        
        editTextSearch.replaceText("le")
        
        recycler {
          hasSize(4)
          childAt<TitleItem>(0) {
            title.hasText("Passwords")
          }
          childAt<PasswordItem>(1) {
            title.hasText("google")
          }
          childAt<TitleItem>(2) {
            title.hasText("Plain texts")
          }
          childAt<PlainTextItem>(3) {
            title.hasText("my title")
          }
        }
        
        editTextSearch.replaceText("gle")
        
        recycler {
          hasSize(2)
          childAt<TitleItem>(0) {
            title.hasText("Passwords")
          }
          childAt<PasswordItem>(1) {
            title.hasText("google")
          }
        }
        
        editTextSearch.replaceText("gmail.com")
        
        recycler {
          hasSize(1)
          firstChild<EmptySearchItem> {
            textNoMatchingEntries.isDisplayed()
          }
        }
        
        imageSearchAction.click()
        
        editTextSearch {
          isDisplayed()
          hasEmptyText()
        }
        recycler.hasSize(5)
        
        editTextSearch.replaceText("google")
        
        recycler.hasSize(2)
        
        closeSoftKeyboard()
        pressBack()
        
        imageSearchAction.hasDrawable(R.drawable.ic_search)
        editTextSearch.isNotDisplayed()
        title.isDisplayed()
        
        imageSearchAction.click()
        menu {
          open()
          exportPasswordsMenuItem.click()
        }
        infoDialog.hide()
        
        editTextSearch.isNotDisplayed()
        title.isDisplayed()
        
        imageSearchAction.click()
        menu {
          open()
          importPasswordsMenuItem.click()
        }
        
        KImportPasswordsScreen {
          pressBack()
        }
        
        editTextSearch.isNotDisplayed()
        title.isDisplayed()
        
        imageSearchAction.click()
        menu {
          open()
          newEntryMenuItem.click()
        }
        entryTypeDialog.passwordEntry.click()
        KPasswordEntryScreen {
          imageBack.click()
        }
        
        editTextSearch.isNotDisplayed()
        title.isDisplayed()
        
        imageSearchAction.click()
        
        menu {
          open()
          settingsMenuItem.click()
        }
        
        KSettingsScreen {
          switchShowUsernames.click()
          imageBack.click()
        }
        
        editTextSearch.isNotDisplayed()
        title.isDisplayed()
        
        imageSearchAction.click()
        editTextSearch.replaceText("gmail.com")
        
        recycler {
          hasSize(2)
          childAt<PasswordItem>(1) {
            title.hasText("google")
            subtitle {
              isDisplayed()
              hasText("me@gmail.com")
            }
          }
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          editTextTitle.replaceText("ggg")
          imageTitleAction.click()
          imageBack.click()
        }
        
        recycler.childAt<PasswordItem>(1) {
          title.hasText("ggg")
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          editTextUsername.replaceText("test")
          imageUsernameAction.click()
          imageBack.click()
        }
        
        recycler.firstChild<EmptySearchItem> { textNoMatchingEntries.isDisplayed() }
        
        editTextSearch.replaceText("g")
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          imageDelete.click()
          confirmationDialog.action2.click()
        }
        
        currentScreenIs<MainListScreen>()
        
        recycler.firstChild<EmptySearchItem> { textNoMatchingEntries.isDisplayed() }
      }
    }
  }
}
