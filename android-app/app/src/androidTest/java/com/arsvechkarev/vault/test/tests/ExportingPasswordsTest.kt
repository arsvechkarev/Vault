package com.arsvechkarev.vault.test.tests

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.matcher.IntentMatchers.hasType
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.export_passwords.ExportPasswordsScreen
import com.arsvechkarev.vault.features.main_list.MainListScreen
import com.arsvechkarev.vault.test.core.base.VaultTestCase
import com.arsvechkarev.vault.test.core.ext.currentScreenIs
import com.arsvechkarev.vault.test.core.ext.hasTextColorInt
import com.arsvechkarev.vault.test.core.ext.writeVaultFileFromAssets
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.core.stub.StubActivityResultSubstitutor
import com.arsvechkarev.vault.test.core.stub.StubPasswordsFileExporter
import com.arsvechkarev.vault.test.screens.KExportPasswordsScreen
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.viewbuilding.Colors
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExportingPasswordsTest : VaultTestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val stubPasswordsFileExporter = StubPasswordsFileExporter()
  
  @Before
  fun setup() = runBlocking {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      activityResultSubstitutor = StubActivityResultSubstitutor(
        stubSelectedFolderUri = "content://myfolder",
        stubCreatedFileUri = "content://myfolder/myfile.png"
      ),
      passwordsFileExporter = stubPasswordsFileExporter
    )
    writeVaultFileFromAssets("file_two_items")
    rule.launchActivity()
  }
  
  @Test
  fun testExportingPasswords() = run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      KMainListScreen {
        menu {
          open()
          exportPasswordsMenuItem.click()
        }
  
        KExportPasswordsScreen {
          currentScreenIs(ExportPasswordsScreen::class)
          iconBack.click()
        }
        
        currentScreenIs(MainListScreen::class)
        
        menu {
          open()
          exportPasswordsMenuItem.click()
        }
        
        KExportPasswordsScreen {
          titleFolder.hasText("Folder")
          textFolder.hasText("Select folder")
          titleFilename.hasText("Filename")
          
          buttonExportPasswords.click()
          
          titleFolder {
            hasTextColorInt(Colors.Error)
            hasText("You have not selected folder")
          }
          titleFilename {
            hasTextColorInt(Colors.Error)
            hasText("Filename is empty")
          }
          
          layoutFolder.click()
          
          titleFolder {
            hasTextColorInt(Colors.Accent)
            hasText("Folder")
          }
          textFolder.hasText("myfolder")
          
          editTextFilename.typeText("a")
          
          titleFilename {
            hasTextColorInt(Colors.Accent)
            hasText("Filename")
          }
          
          editTextFilename.clearText()
          buttonExportPasswords.click()
          
          titleFilename {
            hasTextColorInt(Colors.Error)
            hasText("Filename is empty")
          }
          
          editTextFilename.replaceText("secret_file.mp3")
          buttonExportPasswords.click()
          
          editTextFilename.hasText("myfile.png")
          successDialog {
            title.hasText("Done!")
            message.hasText("Exported passwords successfully")
            action2.hasText("SHARE FILE")
          }
          
          successDialog.action2.click()
          
          flakySafely {
            intended(
              allOf(
                hasAction(Intent.ACTION_CHOOSER),
                hasExtra(Intent.EXTRA_TITLE, "Share"),
                hasExtra(
                  `is`(Intent.EXTRA_INTENT),
                  allOf(
                    hasAction(Intent.ACTION_SEND),
                    hasType(ExportPasswordsScreen.CONTENT_TYPE),
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
}
