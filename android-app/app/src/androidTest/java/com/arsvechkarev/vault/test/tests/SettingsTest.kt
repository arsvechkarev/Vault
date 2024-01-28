package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import com.arsvechkarev.vault.core.views.drawables.LetterInCircleDrawable
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.features.settings.SettingsScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.data.Databases
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.TestImageRequestsRecorder
import com.arsvechkarev.vault.test.core.di.stubs.URL_IMAGE_GOOGLE
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.ext.wasImageRequestWithUrlPerformed
import com.arsvechkarev.vault.test.core.ext.wasNoImageRequestPerformed
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen.PasswordItem
import com.arsvechkarev.vault.test.screens.KSettingsScreen
import org.junit.Rule
import org.junit.Test

class SettingsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val testImageRequestsRecorder = TestImageRequestsRecorder()
  
  @Test
  fun testSettingsScreenFlow() = init {
    rule.launchActivityWithDatabase(Databases.Empty)
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          itemChangePassword.click()
          closeSoftKeyboard()
          pressBack()
          
          currentScreenIs<SettingsScreen>()
          
          imageBack.click()
        }
        
        currentScreenIs<MainListScreen>()
        
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          itemChangePassword.click()
          
          enterPasswordDialog.imageCross.click()
          
          currentScreenIs<SettingsScreen>()
        }
      }
    }
  }
  
  @Test
  fun testImagesLoading() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        imagesRequestsRecorder = testImageRequestsRecorder
      )
    )
    rule.launchActivityWithDatabase(Databases.TwoPasswords)
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler {
          childAt<PasswordItem>(1) {
            image.wasImageRequestWithUrlPerformed(URL_IMAGE_GOOGLE, testImageRequestsRecorder)
          }
        }
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          switchImagesLoading.isEnabled()
          
          switchImagesLoading.click()
          
          pressBack()
        }
        
        recycler {
          childAt<PasswordItem>(1) {
            image.wasNoImageRequestPerformed(testImageRequestsRecorder)
            image.hasDrawable(LetterInCircleDrawable("g"))
          }
        }
      }
    }
    
    rule.finishActivity()
    rule.launchActivity()
    
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        recycler {
          childAt<PasswordItem>(1) {
            image.wasNoImageRequestPerformed(testImageRequestsRecorder)
            image.hasDrawable(LetterInCircleDrawable("g"))
          }
        }
        menu {
          open()
          settingsMenuItem.click()
        }
        KSettingsScreen {
          switchImagesLoading.click()
          pressBack()
        }
        recycler {
          childAt<PasswordItem>(1) {
            image.wasImageRequestWithUrlPerformed(URL_IMAGE_GOOGLE, testImageRequestsRecorder)
          }
        }
      }
    }
  }
}
