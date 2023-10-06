package com.arsvechkarev.vault.test.tests

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.stub.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import domain.EXPORT_CONTENT_TYPE
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.junit.Rule
import org.junit.Test

class ExportPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubPasswordsFileExporter = StubPasswordsFileExporter()
  
  @Test
  fun testExportingPasswords() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      activityResultWrapper = StubActivityResultWrapper(
        stubSelectedFolderUri = "content://myfolder",
        stubCreatedFileUri = "content://myfolder/passwords.kdbx"
      ),
      passwordsFileExporter = stubPasswordsFileExporter
    )
    rule.launchActivityWithDatabase("database_two_passwords")
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        menu {
          open()
          exportPasswordsMenuItem.click()
        }
        infoDialog {
          title.hasText("Done")
          message.hasText("Exported passwords successfully!")
          action2.hasText("SHARE FILE")
          action2.click()
        }
        flakySafely {
          intended(
            allOf(
              hasAction(Intent.ACTION_CHOOSER),
              hasExtra(Intent.EXTRA_TITLE, "Share"),
              hasExtra(
                `is`(Intent.EXTRA_INTENT),
                allOf(
                  hasAction(Intent.ACTION_SEND),
                  hasType(EXPORT_CONTENT_TYPE),
                  hasExtra(Intent.EXTRA_STREAM, stubPasswordsFileExporter.exportingUri)
                )
              )
            )
          )
        }
      }
    }
  }
}
