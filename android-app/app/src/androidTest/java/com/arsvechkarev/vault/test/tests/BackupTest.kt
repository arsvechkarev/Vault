package com.arsvechkarev.vault.test.tests

import androidx.test.core.app.ApplicationProvider
import app.keemobile.kotpass.database.getEntries
import com.arsvechkarev.vault.features.common.di.CoreComponentHolder
import com.arsvechkarev.vault.features.common.domain.DefaultTimestampProvider
import com.arsvechkarev.vault.features.common.domain.SimpleDateTimeFormatter
import com.arsvechkarev.vault.test.core.di.StubExtraDependenciesFactory
import com.arsvechkarev.vault.test.core.di.stubs.StubActivityResultWrapper
import com.arsvechkarev.vault.test.core.di.stubs.TestBackup
import com.arsvechkarev.vault.test.core.di.stubs.TestBackupInterceptor
import com.arsvechkarev.vault.test.core.ext.launchActivityWithDatabase
import com.arsvechkarev.vault.test.core.ext.waitForSnackbarToHide
import com.arsvechkarev.vault.test.core.rule.VaultAutotestRule
import com.arsvechkarev.vault.test.screens.KLoginScreen
import com.arsvechkarev.vault.test.screens.KMainListScreen
import com.arsvechkarev.vault.test.screens.KPasswordEntryScreen
import com.arsvechkarev.vault.test.screens.KSettingsScreen
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import domain.CUSTOM_DATA_FAVORITE_KEY
import domain.CUSTOM_DATA_PASSWORD
import domain.CUSTOM_DATA_NOTE
import domain.CUSTOM_DATA_TYPE_KEY
import domain.Password
import domain.model.PasswordEntryData
import domain.model.NoteEntryData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BackupTest : TestCase() {
  
  @get:Rule
  val rule = VaultAutotestRule()
  
  private val backupInterceptor = TestBackupInterceptor()
  
  private fun passwordItemGoogle(title: String = "google") = PasswordEntryData(
    title = title,
    username = "me@gmail.com",
    password = Password.create("F/<1#E(J=\\51=k;"),
    url = "",
    notes = "",
    isFavorite = false
  )
  
  private val passwordItemTest = PasswordEntryData(
    title = "test.com",
    username = "abcd",
    password = Password.create("q3z;ob15/*8GK>Ed"),
    url = "",
    notes = "",
    isFavorite = false
  )
  
  private val noteItemMyTitle = NoteEntryData(
    title = "my title",
    text = "super secret content",
    isFavorite = false
  )
  
  @Test
  fun testBackup() = init {
    CoreComponentHolder.initialize(
      application = ApplicationProvider.getApplicationContext(),
      factory = StubExtraDependenciesFactory(
        activityResultWrapper = StubActivityResultWrapper(
          stubSelectedFolderUri = "content://com.android.externalstorage.documents/tree/primary%3ABackups"
        ),
        backupInterceptor = backupInterceptor,
      )
    )
    rule.launchActivityWithDatabase("file_two_passwords_and_note")
  }.run {
    KLoginScreen {
      editTextEnterPassword.replaceText("qwetu1233")
      buttonContinue.click()
      
      KMainListScreen {
        assertTrue(backupInterceptor.backups.isEmpty())
        
        menu {
          open()
          settingsMenuItem.click()
        }
        
        KSettingsScreen {
          switchStorageBackup.click()
          
          snackbar.isDisplayedWithText("Storage backup enabled")
          waitForSnackbarToHide()
          
          itemStorageBackupNow.click()
          
          snackbar.isDisplayedWithText("Successfully backed up")
          
          val backup = backupInterceptor.backups.first()
          assertBackupsCount(1)
          assertEntriesCount(3, backup)
          assertHasPasswordEntry(passwordItemGoogle(), backup)
          assertHasPasswordEntry(passwordItemTest, backup)
          assertHasNoteEntry(noteItemMyTitle, backup)
          
          textBackupFolder.hasText("/storage/emulated/0/primary:Backups")
          textLatestBackupDate.hasText("Last backup: ${formattedNowDateTime()}")
          
          pressBack()
        }
        
        recycler.emptyChildAt(1) { click() }
        
        KPasswordEntryScreen {
          editTextTitle.replaceText("1")
          imageTitleAction.click()
          editTextTitle.replaceText("2")
          imageTitleAction.click()
          editTextTitle.replaceText("3")
          imageTitleAction.click()
          editTextTitle.replaceText("4")
          imageTitleAction.click()
          editTextTitle.replaceText("5")
          imageTitleAction.click()
          
          assertBackupsCount(2)
          
          val oldestBackup = backupInterceptor.backups.minByOrNull { it.fileData.lastModified }!!
          assertEntriesCount(3, oldestBackup)
          assertHasPasswordEntry(passwordItemGoogle(), oldestBackup)
          
          val newestBackup = backupInterceptor.backups.maxByOrNull { it.fileData.lastModified }!!
          assertEntriesCount(3, newestBackup)
          assertHasPasswordEntry(passwordItemGoogle(title = "5"), newestBackup)
          
          imageBack.click()
        }
        
        menu {
          open()
          settingsMenuItem.click()
        }
        
        KSettingsScreen {
          itemStorageBackupNow.click()
          
          assertBackupsCount(3)
          
          var newestBackup = backupInterceptor.backups.maxByOrNull { it.fileData.lastModified }!!
          assertEntriesCount(3, newestBackup)
          assertHasPasswordEntry(passwordItemGoogle(title = "5"), newestBackup)
          
          itemStorageBackupNow.click()
          itemStorageBackupNow.click()
          itemStorageBackupNow.click()
          
          assertBackupsCount(5)
          
          val oldestBackup = backupInterceptor.backups.minByOrNull { it.fileData.lastModified }!!
          assertEntriesCount(3, oldestBackup)
          assertHasPasswordEntry(passwordItemGoogle(title = "5"), oldestBackup)
          
          newestBackup = backupInterceptor.backups.maxByOrNull { it.fileData.lastModified }!!
          assertEntriesCount(3, newestBackup)
          assertHasPasswordEntry(passwordItemGoogle(title = "5"), newestBackup)
        }
      }
    }
  }
  
  private fun formattedNowDateTime(): String {
    val timestampProvider = DefaultTimestampProvider()
    return SimpleDateTimeFormatter(timestampProvider).formatReadable(timestampProvider.now())
  }
  
  private fun assertBackupsCount(count: Int) {
    assertEquals(count, backupInterceptor.backups.size)
  }
  
  private fun assertEntriesCount(count: Int, backup: TestBackup) {
    assertEquals(count, backup.database.getEntries { true }.flatMap { it.second }.size)
  }
  
  private fun assertHasPasswordEntry(data: PasswordEntryData, backup: TestBackup) {
    val passwordEntries = backup.database.getEntries { true }
        .flatMap { it.second }
        .filter { it.customData.getValue(CUSTOM_DATA_TYPE_KEY).value == CUSTOM_DATA_PASSWORD }
    val foundEntries = passwordEntries.filter { it.fields.title!!.content == data.title }
    assertEquals("Found zero or more than one password entry", 1, foundEntries.size)
    val passwordEntry = foundEntries.first()
    assertEquals(data.username, passwordEntry.fields.userName!!.content)
    assertEquals(data.password.stringData, passwordEntry.fields.password!!.content)
    assertEquals(data.url, passwordEntry.fields.url!!.content)
    assertEquals(data.notes, passwordEntry.fields.notes!!.content)
    assertEquals(data.isFavorite,
      passwordEntry.customData[CUSTOM_DATA_FAVORITE_KEY]?.value.toBoolean())
  }
  
  private fun assertHasNoteEntry(data: NoteEntryData, backup: TestBackup) {
    val passwordEntries = backup.database.getEntries { true }
        .flatMap { it.second }
        .filter { it.customData.getValue(CUSTOM_DATA_TYPE_KEY).value == CUSTOM_DATA_NOTE }
    val foundEntries = passwordEntries.filter { it.fields.title!!.content == data.title }
    assertEquals("Found zero or more than one note entry", 1, foundEntries.size)
    val passwordEntry = foundEntries.first()
    assertEquals(data.text, passwordEntry.fields.notes!!.content)
    assertEquals(data.isFavorite,
      passwordEntry.customData[CUSTOM_DATA_FAVORITE_KEY]?.value.toBoolean())
  }
}
